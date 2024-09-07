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

/**
 * 根据cn.allin.exposed.table包下的表创建实体类
 */
@OptIn(KspExperimental::class)
fun generatorEntity(resolver: Resolver, codeGenerator: CodeGenerator, logger: KSPLogger) {

    resolver.getDeclarationsFromPackage("cn.allin.exposed.table")
        .mapNotNull { ksDeclaration ->
            logger.warn(ksDeclaration.simpleName.asString())
            val classDeclaration = ksDeclaration.closestClassDeclaration()?:return@mapNotNull null
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