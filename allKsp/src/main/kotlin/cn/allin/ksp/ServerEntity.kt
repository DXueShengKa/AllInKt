package cn.allin.ksp

import cn.allin.ksp.server.EntityToVo
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getConstructors
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
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.U_INT
import com.squareup.kotlinpoet.U_LONG
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 根据cn.allin.exposed.table包下的表创建实体类
 */
@OptIn(KspExperimental::class)
fun generatorEntity(resolver: Resolver, codeGenerator: CodeGenerator, logger: KSPLogger) {

    resolver.getDeclarationsFromPackage("cn.allin.exposed.table")
        .mapNotNull { ksDeclaration ->
            logger.warn(ksDeclaration.simpleName.asString())
            val classDeclaration = ksDeclaration.closestClassDeclaration() ?: return@mapNotNull null
            val fileNotNull = classDeclaration.containingFile != null
            if (classDeclaration.superTypes.any {
                    it.resolve().declaration.simpleName.asString().endsWith("IdTable")
                } && fileNotNull) {
                classDeclaration
            } else {
                null
            }
        }
        .forEach { d ->
            val tableClass = d.toClassName()
            val entityClassName = ClassName(
                "cn.allin.exposed.entity",
                tableClass.simpleName.run { substring(0, indexOf("Table")) } + "Entity"
            )

            val idType = d.superTypes.first().resolve().declaration.simpleName.asString().run {
                substring(0, indexOf("IdTable"))
            }
            val daoPackage = "org.jetbrains.exposed.dao"

            val typeSpec = TypeSpec.classBuilder(entityClassName.simpleName)
                .superclass(ClassName(daoPackage, "${idType}Entity"))
                .addSuperclassConstructorParameter("id")
                .primaryConstructor(
                    FunSpec.constructorBuilder().addParameter(
                        "id",
                        ClassName("$daoPackage.id", "EntityID").parameterizedBy(
                            when (idType) {
                                INT.simpleName -> INT
                                U_INT.simpleName -> U_INT
                                LONG.simpleName -> LONG
                                U_LONG.simpleName -> U_LONG
                                else -> error("未支持的类型")
                            }
                        )
                    ).build()
                )
                .addType(
                    TypeSpec.companionObjectBuilder()
                        .superclass(
                            ClassName(daoPackage, "${idType}EntityClass")
                                .parameterizedBy(entityClassName)
                        )
                        .addSuperclassConstructorParameter("%T", tableClass)
                        .build()
                )


            d.getDeclaredProperties()
                .forEach {
                    val propertyName = it.simpleName.asString()
                    val propertySpec = PropertySpec.builder(
                        propertyName,
                        type = it.type.resolve().arguments.first().type!!.toTypeName()
                    )
                        .delegate("%T.$propertyName", tableClass)
                        .mutable(true)
                    typeSpec.addProperty(propertySpec.build())
                }

            val file = FileSpec.builder(entityClassName)
                .addType(typeSpec.build())
                .build()

            codeGenerator.createNewFile(
                Dependencies(true, d.containingFile!!),
                entityClassName.packageName,
                entityClassName.simpleName,
            ).writer().use {
                file.writeTo(it)
            }
        }
}

private class VoField(
    val name: String,
    val type: KSType
)

private class VoType(
    val voName: ClassName,
    val fields: List<VoField>
)

private val JavaLocalDate = LocalDate::class.asClassName()
private val JavaLocalDateTime = LocalDateTime::class.asClassName()


@OptIn(KspExperimental::class)
fun generatorEntityToVo(resolver: Resolver, codeGenerator: CodeGenerator, logger: KSPLogger) {
    val toVoName = EntityToVo::class.java.name

    val fs = FileSpec.builder("cn.allin.utils", "EntityToVo")
        .addImport("kotlinx.datetime", "toKotlinLocalDate")
        .addImport("kotlinx.datetime", "toKotlinLocalDateTime")

    val d = mutableListOf<KSFile>()

    resolver.getSymbolsWithAnnotation(toVoName)
        .forEach { a ->
            val annotationValue: List<KSType> = a.annotations.first {
                toVoName.endsWith(it.shortName.getShortName())
            }.arguments
                .first()
                .value as List<KSType>

            val voSeq = annotationValue.asSequence()
                .map { type ->
                    val voFieldList = (type.declaration as KSClassDeclaration)
                        .getConstructors()
                        .maxBy { c ->
                            c.parameters.size
                        }.parameters
                        .map {
                            VoField(it.name!!.getShortName(), it.type.resolve())
                        }
                    VoType(type.toClassName(), voFieldList)
                }

            a.accept(GenerateToVo(logger, voSeq), fs)

            a.containingFile?.also(d::add)
        }

       fs.build()
        .writeTo(codeGenerator, Dependencies(false, *d.toTypedArray()))

}


private class GenerateToVo(private val logger: KSPLogger, private val voTypes: Sequence<VoType>) : KSEmptyVisitor<FileSpec.Builder, Unit>() {


    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: FileSpec.Builder) {

        val entityField = classDeclaration.getDeclaredProperties()
            .associate {
                it.simpleName.asString() to it.type.resolve()
            }

        voTypes.forEach {
            val code: CodeBlock.Builder = CodeBlock.builder()
                .add("return %T(", it.voName)

            for (field in it.fields) {
                val entityTypeDeclaration: KSClassDeclaration = entityField[field.name]?.declaration as? KSClassDeclaration ?: continue

                val right: String = if (field.type.declaration.qualifiedName == entityTypeDeclaration.qualifiedName) {
                    field.name
                } else if (entityTypeDeclaration.classKind == ClassKind.ENUM_CLASS) {
                    when (field.type.declaration.simpleName.getShortName()) {
                        "String" -> "${field.name}.name"
                        "Int" -> "${field.name}.ordinal"
                        else -> continue
                    }
                } else if (entityTypeDeclaration.toClassName() == JavaLocalDate) {
                    "${field.name}?.toKotlinLocalDate()"
                } else if (entityTypeDeclaration.toClassName() == JavaLocalDateTime) {
                    "${field.name}?.toKotlinLocalDateTime()"
                } else {
                    continue
                }

                code.add("${field.name} = $right, ")
            }

            val funSpec = FunSpec.builder("to${it.voName.simpleName}")
                .receiver(classDeclaration.toClassName())
                .returns(it.voName)
                .addCode(code.add(")").build())
                .build()

            data.addFunction(funSpec)
        }

    }

    override fun defaultHandler(
        node: KSNode,
        data: FileSpec.Builder
    ) {

    }

}

fun generatorSerializationField(resolver: Resolver, codeGenerator: CodeGenerator, logger: KSPLogger) {

    val typeSpec = TypeSpec.objectBuilder("VoFieldName")
        .addKdoc("vo类的字段名")

    val d = mutableListOf<KSFile>()

    resolver.getSymbolsWithAnnotation("kotlinx.serialization.Serializable")
        .flatMap { a ->
            a.containingFile?.declarations?.mapNotNull {
                it.closestClassDeclaration()
            } ?: emptySequence()
        }
        .forEach { d ->

            if (d.typeParameters.isNotEmpty())
                return@forEach

            val className = d.simpleName.asString()
            val stringType = String::class.asTypeName()
//            val receiverType = ClassName(d.packageName.asString(), className, "Companion")

            val constructor = d.getConstructors().first()
            for (p in constructor.parameters) {
                val name = p.name?.asString() ?: continue
                val constName = "${className}_$name"
                typeSpec.addProperty(
                    PropertySpec.builder(constName, stringType, KModifier.CONST)
                        .initializer("\"$name\"")
                        .build()
                )

//                val propertySpec = PropertySpec.builder(name, stringType)
//                    .receiver(receiverType)
//                    .getter(
//                        FunSpec.getterBuilder()
//                            .addModifiers(KModifier.INLINE)
//                            .addCode("return $constName")
//                            .build()
//                    )
//                    .build()
//                typeSpec.addProperty(propertySpec)
            }
        }

    FileSpec.builder("cn.allin", "VoFieldName")
        .addType(typeSpec.build())
        .build()
        .writeTo(codeGenerator, Dependencies(false, *d.toTypedArray()))

}
