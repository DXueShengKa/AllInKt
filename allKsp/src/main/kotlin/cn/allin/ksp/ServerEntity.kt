package cn.allin.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.ClassName
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
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

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
