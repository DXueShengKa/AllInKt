package cn.allin.ksp

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * 生成字段名字
 */
fun generatorSerializationField(resolver: Resolver, codeGenerator: CodeGenerator, logger: KSPLogger) {

    val typeSpec = TypeSpec.objectBuilder("VoFieldName")
        .addKdoc("vo类的字段名")

    val d = mutableListOf<KSFile>()

    val stringType = String::class.asTypeName()

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

const val OPTIONS_IS_TEST = "isTest"
const val OPTIONS_PRE_RELEASE = "pre_release"

/**
 * kotlin通用版本BuildConfig
 */
fun generateBuildConfig(codeGenerator: CodeGenerator, options: Map<String, String>, moduleName: String) {
    val fileName = "BuildConfig"

    val test = options.getOrDefault(OPTIONS_IS_TEST, "false")
    val preRelease = options.getOrDefault(OPTIONS_PRE_RELEASE, "false")

    val obj = TypeSpec.objectBuilder(fileName)
        .addProperty(
            PropertySpec.builder("TEST", Boolean::class)
                .addModifiers(KModifier.CONST, KModifier.PUBLIC)
                .initializer(test)
                .addKdoc("是否为测试api环境")
                .build()
        )
        .addProperty(
            PropertySpec.builder("DEBUG", Boolean::class)
                .addModifiers(KModifier.CONST, KModifier.PUBLIC)
//                .initializer(moduleName.lowercase().contains("debug").toString())
                .initializer("TEST")
                .build()
        )
        .addProperty(
            PropertySpec.builder("PRE_RELEASE", Boolean::class)
                .addModifiers(KModifier.CONST, KModifier.PUBLIC)
                .initializer(preRelease)
                .addKdoc("预发布版本")
                .build()
        )
        .addKdoc("module: $moduleName")
        .build()

    FileSpec.builder("cn.allin", fileName)
        .addType(obj)
        .build()
        .writeTo(codeGenerator, false)
}
