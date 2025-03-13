package cn.allin.ksp.viewmodel

import cn.allin.ksp.getSubclassesByType
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.getKotlinClassByName
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.writeTo


/**
 * 生成ViewModel的koin工厂代码
 */
@OptIn(KspExperimental::class)
fun generateViewModel(
    moduleName:String,
    codeGenerator: CodeGenerator,
    resolver: Resolver,
    filePackageName: String
) {
    val fileName = "${moduleName}ViewModel"

    val vmType = resolver.getKotlinClassByName("androidx.lifecycle.ViewModel")!!.asStarProjectedType()

    val moduleCode = CodeBlock.builder()
        .beginControlFlow("org.koin.dsl.module")

    val originatingKSFiles = resolver.getAllFiles()
        //获取vm类
        .getSubclassesByType(vmType)
        .mapNotNull { declaration ->

            if (Modifier.ABSTRACT in declaration.modifiers) return@mapNotNull null

            val viewModelName = declaration.run { "${packageName.asString()}.${simpleName.asString()}" }
            //构造参数
            val params = declaration.getConstructors().first()
                .parameters
                .joinToString(",", "(", ")") { parameter ->
                    val paramName = parameter.name?.asString()?.plus("=") ?: ""
                    val parametersVM =
                        if (parameter.annotations.any { it.shortName.asString() == ParametersVM::class.simpleName })
                            "it."
                        else ""

                    if (parameter.type.resolve().nullability == Nullability.NULLABLE)
                        "${paramName}${parametersVM}getOrNull()"
                    else
                        "${paramName}${parametersVM}get()"
                }

            moduleCode.factory(viewModelName + params)
            declaration.containingFile
        }
        .toList()


    val property = PropertySpec.builder("${moduleName}KoinViewModel", ClassName("org.koin.core.module", "Module"))
        .addKdoc("ksp生成ViewModel的koin配置")
        .initializer(moduleCode.endControlFlow().build())
        .build()

    FileSpec.builder(filePackageName, fileName)
        .addProperty(property)
        .addImport("org.koin.core.module.dsl","viewModel")
        .build()
        .writeTo(codeGenerator, true, originatingKSFiles)
}

private fun CodeBlock.Builder.factory(str: String): CodeBlock.Builder {
    return beginControlFlow("viewModel")
        .addStatement(str)
        .endControlFlow()
}

