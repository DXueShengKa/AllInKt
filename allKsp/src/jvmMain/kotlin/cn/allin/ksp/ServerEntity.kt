package cn.allin.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

@OptIn(KspExperimental::class)
fun generatorEntity(resolver: Resolver, codeGenerator: CodeGenerator, logger: KSPLogger) {

    resolver.getDeclarationsFromPackage("cn.allin.exposed.table")
        .mapNotNull { ksDeclaration ->
            logger.warn(ksDeclaration.simpleName.asString())
            val classDeclaration = ksDeclaration.closestClassDeclaration()
            if (classDeclaration?.superTypes?.any {
                    it.resolve().declaration.simpleName.asString().endsWith("IdTable")
                } == true) {
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
                        ClassName("$daoPackage.id", "EntityID").parameterizedBy(INT)
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
                    val t = it.type.resolve().arguments.first().type!!.toTypeName()
                    val propertyName = it.simpleName.asString()
                    val propertySpec = PropertySpec.builder(
                        propertyName,
                        t
                    ).delegate("%T.$propertyName", tableClass)
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