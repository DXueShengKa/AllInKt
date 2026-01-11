package cn.allin.ksp

import cn.allin.ksp.server.EntityToVo
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.U_INT
import com.squareup.kotlinpoet.U_LONG
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 根据cn.allin.exposed.table包下的表创建实体类
 */
@OptIn(KspExperimental::class)
fun generatorEntity(
    resolver: Resolver,
    codeGenerator: CodeGenerator,
    logger: KSPLogger,
) {
    resolver
        .getDeclarationsFromPackage("cn.allin.exposed.table")
        .mapNotNull { ksDeclaration ->
            logger.warn(ksDeclaration.simpleName.asString())
            val classDeclaration = ksDeclaration.closestClassDeclaration() ?: return@mapNotNull null
            val fileNotNull = classDeclaration.containingFile != null
            if (classDeclaration.superTypes.any {
                    it
                        .resolve()
                        .declaration.simpleName
                        .asString()
                        .endsWith("IdTable")
                } && fileNotNull
            ) {
                classDeclaration
            } else {
                null
            }
        }.forEach { d ->
            val tableClass = d.toClassName()
            val entityClassName =
                ClassName(
                    "cn.allin.exposed.entity",
                    tableClass.simpleName.run { substring(0, indexOf("Table")) } + "Entity",
                )

            val idType =
                d.superTypes.first().resolve().declaration.simpleName.asString().run {
                    substring(0, indexOf("IdTable"))
                }
            val daoPackage = "org.jetbrains.exposed.dao"

            val typeSpec =
                TypeSpec
                    .classBuilder(entityClassName.simpleName)
                    .superclass(ClassName(daoPackage, "${idType}Entity"))
                    .addSuperclassConstructorParameter("id")
                    .primaryConstructor(
                        FunSpec
                            .constructorBuilder()
                            .addParameter(
                                "id",
                                ClassName("$daoPackage.id", "EntityID").parameterizedBy(
                                    when (idType) {
                                        INT.simpleName -> INT
                                        U_INT.simpleName -> U_INT
                                        LONG.simpleName -> LONG
                                        U_LONG.simpleName -> U_LONG
                                        else -> error("未支持的类型")
                                    },
                                ),
                            ).build(),
                    ).addType(
                        TypeSpec
                            .companionObjectBuilder()
                            .superclass(
                                ClassName(daoPackage, "${idType}EntityClass")
                                    .parameterizedBy(entityClassName),
                            ).addSuperclassConstructorParameter("%T", tableClass)
                            .build(),
                    )

            d
                .getDeclaredProperties()
                .forEach {
                    val propertyName = it.simpleName.asString()
                    val propertySpec =
                        PropertySpec
                            .builder(
                                propertyName,
                                type =
                                    it.type
                                        .resolve()
                                        .arguments
                                        .first()
                                        .type!!
                                        .toTypeName(),
                            ).delegate("%T.$propertyName", tableClass)
                            .mutable(true)
                    typeSpec.addProperty(propertySpec.build())
                }

            val file =
                FileSpec
                    .builder(entityClassName)
                    .addType(typeSpec.build())
                    .build()

            codeGenerator
                .createNewFile(
                    Dependencies(true, d.containingFile!!),
                    entityClassName.packageName,
                    entityClassName.simpleName,
                ).writer()
                .use {
                    file.writeTo(it)
                }
        }
}

private class VoField(
    val name: String,
    val type: KSType,
)

private class VoType(
    val voName: ClassName,
    val fields: List<VoField>,
)

private val JavaLocalDate = LocalDate::class.asClassName()
private val JavaLocalDateTime = LocalDateTime::class.asClassName()

@OptIn(KspExperimental::class)
fun generatorEntityToVo(
    resolver: Resolver,
    codeGenerator: CodeGenerator,
    logger: KSPLogger,
) {
    val toVoName = EntityToVo::class.java.name

    val fs =
        FileSpec
            .builder("cn.allin.utils", "EntityToVo")
            .addImport("kotlinx.datetime", "toKotlinLocalDate")
            .addImport("kotlinx.datetime", "toKotlinLocalDateTime")
            .addImport("kotlinx.datetime", "toJavaLocalDateTime")

    val d = mutableListOf<KSFile>()

    resolver
        .getSymbolsWithAnnotation(toVoName)
        .forEach { a ->
//            val entity = a.containingFile!!.declarations.filterIsInstance<KSClassDeclaration>()
//                .first()
//                .getDeclaredProperties()

            val annotationValue: List<KSType>? =
                a.annotations
                    .first {
                        toVoName.endsWith(it.shortName.getShortName())
                    }.arguments
                    .first()
                    .value as? List<KSType>

            val voSeq =
                annotationValue
                    ?.asSequence()
                    ?.map { type ->
                        val voFieldList =
                            (type.declaration as KSClassDeclaration)
                                .primaryConstructor!!
                                .parameters
                                .map {
                                    val fieldName = it.name!!.asString()
                                    VoField(
                                        fieldName,
                                        it.type.resolve(),
                                    )
                                }

                        logger.warn(type.toClassName().reflectionName())
                        VoType(type.toClassName(), voFieldList)
                    }

            a.accept(GenerateToVo(logger, voSeq ?: emptySequence()), fs)

            a.containingFile?.also(d::add)
        }

    fs
        .build()
        .writeTo(codeGenerator, Dependencies(false, *d.toTypedArray()))
}

private class GenerateToVo(
    private val logger: KSPLogger,
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
                    it.simpleName.asString() to it.type.resolve()
                }

        voTypes.forEach {
            data.addFunction(toVoFun(it, entityField, classDeclaration.toClassName()))

            data.addFunction(toEntityFun(it, entityField, classDeclaration.toClassName()))
        }
    }

    private fun toVoFun(
        voType: VoType,
        entityField: Map<String, KSType>,
        entityType: ClassName,
    ): FunSpec {
        val code: CodeBlock.Builder =
            CodeBlock
                .builder()
                .add("return %T(", voType.voName)

        for (field in voType.fields) {
            val entityTypeDeclaration: KSClassDeclaration = entityField[field.name]?.declaration as? KSClassDeclaration ?: continue

            val nullability = if (field.type.isMarkedNullable) "?" else ""

            val right: String =
                if (field.type.declaration.qualifiedName == entityTypeDeclaration.qualifiedName) {
                    field.name
                } else if (entityTypeDeclaration.classKind == ClassKind.ENUM_CLASS) {
                    when (
                        field.type.declaration.simpleName
                            .getShortName()
                    ) {
                        "String" -> "${field.name}$nullability.name"
                        "Int" -> "${field.name}$nullability.ordinal"
                        else -> continue
                    }
                } else if (entityTypeDeclaration.toClassName() == JavaLocalDate) {
                    "${field.name}$nullability.toKotlinLocalDate()"
                } else if (entityTypeDeclaration.toClassName() == JavaLocalDateTime) {
                    "${field.name}$nullability.toKotlinLocalDateTime()"
                } else {
                    continue
                }

            code.add("${field.name} = $right, ")
        }

        return FunSpec
            .builder("to${voType.voName.simpleName}")
            .receiver(entityType)
            .returns(voType.voName)
            .addCode(code.add(")").build())
            .build()
    }

    private fun toEntityFun(
        voType: VoType,
        entityField: Map<String, KSType>,
        entityType: ClassName,
    ): FunSpec {
        val code: CodeBlock.Builder =
            CodeBlock
                .builder()
                .add("return %T {", entityType)

        for (field in voType.fields) {
            val entityTypeDeclaration: KSClassDeclaration = entityField[field.name]?.declaration as? KSClassDeclaration ?: continue

            val right: String =
                if (field.type.declaration.qualifiedName == entityTypeDeclaration.qualifiedName) {
                    "this@toEntity." + field.name
                } else if (entityTypeDeclaration.classKind == ClassKind.ENUM_CLASS) {
                    val q = "this@toEntity.${field.name}"
                    val z =
                        when (
                            field.type.declaration.simpleName
                                .getShortName()
                        ) {
                            "String" -> entityTypeDeclaration.qualifiedName?.asString() + ".valueOf(it)"
                            "Int" -> entityTypeDeclaration.qualifiedName?.asString() + ".entries[it]"
                            else -> continue
                        }
                    "${q}${if (field.type.isMarkedNullable) '?' else ""}.let { $z }"
                } else if (entityTypeDeclaration.toClassName() == JavaLocalDateTime) {
                    "this@toEntity." + "${field.name}?.toJavaLocalDateTime()"
                } else {
                    continue
                }

            if (field.name == "id") {
                code.addStatement("\nif(id > 0)")
            }

            if (field.type.isMarkedNullable || entityField[field.name]?.isMarkedNullable == true) {
                code.addStatement("\t$right?.also { ${field.name} = it }")
            } else {
                code.addStatement("\t${field.name} = $right")
            }
        }

        return FunSpec
            .builder("toEntity")
            .receiver(voType.voName)
            .returns(entityType)
            .addCode(code.add("}").build())
            .build()
    }

    override fun defaultHandler(
        node: KSNode,
        data: FileSpec.Builder,
    ) {
    }
}
