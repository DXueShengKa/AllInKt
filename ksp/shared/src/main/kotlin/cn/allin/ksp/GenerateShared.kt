package cn.allin.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.closestClassDeclaration
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
import org.jetbrains.dokka.InternalDokkaApi
import org.jetbrains.dokka.analysis.markdown.jb.MarkdownParser
import org.jetbrains.dokka.model.doc.Param
import org.jetbrains.dokka.model.doc.Text
import org.jetbrains.dokka.model.firstMemberOfTypeOrNull


@OptIn(InternalDokkaApi::class)
fun MarkdownParser.getParamList(logger: KSPLogger, text: String?): Map<String, String?> {
    text ?: return emptyMap()
    val paramList = hashMapOf<String, String?>()
    parse(text.trimIndent()).children.forEach { tagWrapper ->
        when (tagWrapper) {
            is Param -> {
                paramList[tagWrapper.name] = tagWrapper.firstMemberOfTypeOrNull<Text>()?.body
            }
//            is Description -> {
//
//            }
            else -> {
                logger.info("doc $tagWrapper")
            }
        }
    }

    return paramList
}


/**
 * 生成字段名字
 */
@OptIn(KspExperimental::class, InternalDokkaApi::class)
fun generatorSerializationField(resolver: Resolver, codeGenerator: CodeGenerator, logger: KSPLogger) {

    val typeSpec = TypeSpec.objectBuilder("VoFieldName")
        .addKdoc("vo类的字段名")

    val d = mutableListOf<KSFile>()

    val stringType = String::class.asTypeName()
    val md = MarkdownParser({ null }, "")
    resolver.getDeclarationsFromPackage("cn.allin.vo")
        .mapNotNull {
            it.closestClassDeclaration()
        }
        .filter { declaration ->
            declaration.annotations.any { it.shortName.getShortName() == "Serializable" }
        }
//    resolver.getSymbolsWithAnnotation("kotlinx.serialization.Serializable")
//        .flatMap { a ->
//            a.containingFile?.declarations?.mapNotNull {
//                it.closestClassDeclaration()
//            } ?: emptySequence()
//        }
        .forEach { d ->

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
