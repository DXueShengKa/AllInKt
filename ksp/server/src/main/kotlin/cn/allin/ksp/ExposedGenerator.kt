package cn.allin.ksp

import cn.allin.ksp.server.TableToVo
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

private val ResultRowClassName = ClassName("org.jetbrains.exposed.v1.core", "ResultRow")
private val EntityIDClassName = ClassName("org.jetbrains.exposed.v1.core.dao.id", "EntityID")

@Suppress("ktlint:standard:backing-property-naming")
private val _UpdateBuilderClassName = ClassName("org.jetbrains.exposed.v1.core.statements", "UpdateBuilder")
private val InsertStatementClassName = ClassName("org.jetbrains.exposed.v1.core.statements", "InsertStatement")
private val UpdateStatementClassName = ClassName("org.jetbrains.exposed.v1.core.statements", "UpdateStatement")

private val UpdateBuilderClassName = _UpdateBuilderClassName.parameterizedBy(ANY)

private val InsertStatementLongClassName =
    InsertStatementClassName.parameterizedBy(
        EntityIDClassName.parameterizedBy(
            LONG,
        ),
    )

private val InsertStatementIntClassName =
    InsertStatementClassName.parameterizedBy(
        EntityIDClassName.parameterizedBy(
            INT,
        ),
    )

@Suppress("UNCHECKED_CAST")
@OptIn(KspExperimental::class)
fun tableToVo(
    resolver: Resolver,
    codeGenerator: CodeGenerator,
    logger: KSPLogger,
) {
    val toVoName = TableToVo::class.java.name

    val d = mutableListOf<KSFile>()

    val fs =
        FileSpec
            .builder("cn.allin.model", "EntityToVo")
            .addImport(ResultRowClassName.packageName, ResultRowClassName.simpleName)
            .addAnnotation(
                AnnotationSpec
                    .builder(ClassName("kotlin", "Suppress"))
                    .addMember("\"UNCHECKED_CAST\"")
                    .build(),
            )

    resolver
        .getSymbolsWithAnnotation(toVoName)
        .forEach { a ->
            a as KSClassDeclaration

            val voList =
                a.annotations
                    .first { it.shortName.asString() == TableToVo::class.simpleName }
                    .arguments
                    .first { it.name!!.asString() == "vo" }
                    .value as List<KSType>

            val voTypes =
                voList
                    .asSequence()
                    .mapNotNull {
                        it.declaration as? KSClassDeclaration
                    }.map { d ->
                        val fields =
                            d.primaryConstructor!!
                                .parameters
                                .map {
                                    VoField(
                                        it.name!!.asString(),
                                        it.type.resolve(),
                                    )
                                }
                        VoType(
                            voName = d.toClassName(),
                            fields = fields,
                        )
                    }

            a.accept(GenerateTableToVo(logger, resolver, voTypes), fs)
            a.containingFile?.also(d::add)
        }

    fs
        .build()
        .writeTo(codeGenerator, Dependencies(false, *d.toTypedArray()))
}

private class GenerateTableToVo(
    private val logger: KSPLogger,
    private val resolver: Resolver,
    private val voTypes: Sequence<VoType>,
) : KSEmptyVisitor<FileSpec.Builder, Unit>() {
    override fun visitClassDeclaration(
        classDeclaration: KSClassDeclaration,
        data: FileSpec.Builder,
    ) {
        val entityField =
            classDeclaration
                .getDeclaredProperties()
                .associate {
                    val ksType = it.type.resolve()
                    val tn =
                        (
                            ksType.arguments
                                .getOrNull(0)
                                ?.type
                                ?.resolve() ?: ksType
                        )
                    it.simpleName.asString() to tn
                } as MutableMap<String, KSType>

        classDeclaration.superTypes.forEach {
            val name =
                it
                    .resolve()
                    .declaration.simpleName
                    .asString()

            when (name) {
                "LongIdTable" -> {
                    entityField["id"] = resolver.builtIns.longType
                }

                "IntIdTable" -> {
                    entityField["id"] = resolver.builtIns.intType
                }
            }
        }
        val tableType = classDeclaration.toClassName()

        voTypes.forEach {
            data.addFunction(toVoFun(it, entityField, tableType))

            val isInt = entityField["id"] == resolver.builtIns.intType

            data.addFunction(
                updateFun(
                    it,
                    entityField,
                    tableType,
                ),
            )

            data.addFunction(
                FunSpec
                    .builder("updateBuilder")
                    .addModifiers(KModifier.INLINE)
                    .receiver(tableType)
                    .addParameter("update", if (isInt) InsertStatementIntClassName else InsertStatementLongClassName)
                    .addParameter("vo", it.voName)
                    .addCode("updateBuilder(update as %T,vo)", UpdateBuilderClassName)
                    .build(),
            )

            data.addFunction(
                FunSpec
                    .builder("updateBuilder")
                    .addModifiers(KModifier.INLINE)
                    .receiver(tableType)
                    .addParameter("update", UpdateStatementClassName)
                    .addParameter("vo", it.voName)
                    .addCode("updateBuilder(update as %T,vo)", UpdateBuilderClassName)
                    .build(),
            )
        }
    }

    private fun toVoFun(
        voType: VoType,
        entityField: Map<String, KSType>,
        tableType: ClassName,
    ): FunSpec {
        val code: CodeBlock.Builder =
            CodeBlock
                .builder()
                .add("return %T(", voType.voName)

        for (field in voType.fields) {
            val entityType = entityField[field.name] ?: continue

            logger.warn(field.name)

            val nullability = if (field.type.isMarkedNullable) "?" else ""

            val value =
                if (field.name.equals("id", true)) {
                    ".value"
                } else {
                    ""
                }
            val right: String =
                if (entityType.declaration.simpleName.asString() ==
                    field.type.declaration.simpleName
                        .asString()
                ) {
                    field.name
                } else {
                    continue
                }
            code.addStatement("${field.name} = get(%T.$right)$value, ", tableType)
        }

        return FunSpec
            .builder("to${voType.voName.simpleName}")
            .receiver(ResultRowClassName)
            .returns(voType.voName)
            .addCode(code.add(")").build())
            .build()
    }

    private fun updateFun(
        voType: VoType,
        tableField: Map<String, KSType>,
        tableType: ClassName,
    ): FunSpec {
        val code: CodeBlock.Builder =
            CodeBlock
                .builder()

        for (field in voType.fields) {
            if (field.name == "id") continue
            val tableType = tableField[field.name] ?: continue

            var r = "it"

            if (tableType.declaration.isEnumClass) {
                val eName = tableType.declaration.qualifiedName?.asString()
                when (
                    field.type.declaration.simpleName
                        .asString()
                ) {
                    "String" -> r = "$eName.valueOf(it)"
                    "Int" -> r = "$eName.valueOf(it)"
                }
            }

            val isNull = field.type.isMarkedNullable
            if (isNull) {
                code.addStatement("vo.${field.name}?.also { update[${field.name}] = $r }")
            } else {
                code.addStatement("update[${field.name}] = vo.${field.name}")
            }
        }

        return FunSpec
            .builder("updateBuilder")
            .receiver(tableType)
            .addParameter("update", UpdateBuilderClassName)
            .addParameter("vo", voType.voName)
            .addCode(code.build())
            .build()
    }

    override fun defaultHandler(
        node: KSNode,
        data: FileSpec.Builder,
    ) {
    }
}

val KSDeclaration?.isEnumClass: Boolean
    get() = if (this == null) false else ClassKind.ENUM_CLASS == (this as? KSClassDeclaration)?.classKind
