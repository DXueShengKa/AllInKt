package cn.allin.ksp.navigation

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.isInternal
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Nullability
import com.google.devtools.ksp.validate
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.writeTo


private const val PACKAGE_NAME = "cn.allin.navigation"

private const val NavTypePackage = "cn.allin.ksp.navigation"

class GenerateNav(
    private val codeGenerator: CodeGenerator,
    private val resolver: Resolver,
    private val logger: KSPLogger,
    private val fileName: String
) {
    //依赖关联这些文件，这些文件发生改变时触发ksp更改
    private val originatingKSFiles = mutableListOf<KSFile>()

    //无法处理的注解
    val notValidate = mutableListOf<KSAnnotated>()


    private val fileSpec = FileSpec.builder(PACKAGE_NAME, "Routes$fileName")


    /**
     * 根据[NavRoute]注解生成导航dsl，路由的名字要以Route开头，统一提示
     */
    fun generateNavGraphDsl() {

        logger.info("生成路由导航dsl")

        val blockBuilder = CodeBlock.builder()

        resolver.getSymbolsWithAnnotation(NavRoute::class.java.name)
            .forEach {
                if (it.validate()) {
                    it.accept(GenerateNavDsl(logger), blockBuilder)
                    it.containingFile?.also(originatingKSFiles::add)
                } else {
                    notValidate.add(it)
                }
            }

        val funSpec = FunSpec.builder("${fileName.lowercase()}NavGraphs")
            .addModifiers(KModifier.INTERNAL)
            .receiver(ClassName("androidx.navigation", "NavGraphBuilder"))
            .addCode(blockBuilder.build())
            .addKdoc("使用类进行导航")
            .build()

        fileSpec
            .addFunction(funSpec)
            .addImport("androidx.navigation.compose", "composable")
            .addImport("androidx.navigation", "toRoute")
            .addImport(NavTypePackage, "KSerializerNavType")
            .addImport(NavTypePackage, "ByteArrayNavType")
            .addImport(NavTypePackage, "EnumNavType")
            .addImport("kotlin.reflect", "typeOf")
            .addImport("kotlinx.serialization", "serializer")
    }

    fun writeFile() {
        fileSpec.build().writeTo(codeGenerator, true, originatingKSFiles)
    }
}

private const val ROUTE = "Route"

private class GenerateNavDsl(private val logger: KSPLogger) : KSEmptyVisitor<CodeBlock.Builder, Unit>() {


    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: CodeBlock.Builder) {

        val navRoute = function.annotations.first { it.shortName.getShortName() == NavRoute::class.simpleName }

        var routeType: KSType? = null
        var routeStr = ""
        navRoute.arguments.forEach { va ->
           when (va.name?.asString()) {
               "routeType" -> {
                   routeType = va.value as? KSType
               }
               "routeString" -> {
                   va.value?.also {
                       routeStr = it as String
                   }
               }
           }
        }


        if (routeStr.isNotEmpty()
            //多平台代码读取到公共目录下的声明文件需要单独搜索io，先跳过验证
            && Modifier.ACTUAL !in function.modifiers
        ) {
            check(function.containingFile!!.declarations
                .mapNotNull {
                    it as? KSPropertyDeclaration
                }
                .any {
                    (it.isPublic() || it.isInternal())
                            && it.type.resolve().declaration.simpleName.asString() == "String"
                            && it.simpleName.getShortName().startsWith(ROUTE)
                }
            ) { "使用routeStr参数时，必须使用一个在同一个文件声明的以Route开头的常数字符串作为参数, ${function.qualifiedName?.asString()}" }
        }

        //如果是NavGraphBuilder的扩展函数，那么直接添加到dsl中
        if (function.extensionReceiver?.resolve()?.declaration?.simpleName?.asString() == "NavGraphBuilder") {
            data.addStatement("%T()", ClassName(function.packageName.asString(), function.simpleName.asString()))
        } else if (routeStr.isNotEmpty()) { //生成字符串路由
            check(function.parameters.size < 2) { "最多一个参数" }

            val parameter = function.parameters.firstOrNull()
            val params = if (parameter != null) {
                val typeName = parameter.type.resolve().canonicalName()
                if (typeName != "androidx.navigation.NavBackStackEntry") error("只能有一个NavBackStackEntry参数")

                parameter.name?.asString() + " = it"
            } else ""

            data.beginControlFlow("composable(route = \"$routeStr\")")
                .addStatement("${function.qualifiedName?.asString()}(${params})")
                .endControlFlow()

        } else { //生成类型路由

            checkType(routeType!!, function)

            val params = function.parameters.mapNotNull {
                val paramType = it.type.resolve()
                //参数中存在路由类
                when {
                    paramType == routeType -> {
                        if (!paramType.declaration.annotations.any { a -> a.shortName.getShortName() == "Immutable" }) {
                            logger.warn("建议 ${routeType.canonicalName()} 加上@Immutable以减少compose重组")
                        }
                        it.name!!.asString() + " = it.toRoute()"
                    }

                    paramType.canonicalName() == "androidx.navigation.NavBackStackEntry" -> {
                        it.name!!.asString() + " = it"
                    }

                    paramType.canonicalName() == "androidx.compose.animation.AnimatedContentScope" -> {
                        "this"
                    }

                    else -> null
                }
            }.joinToString()


            val routeDeclaration:KSClassDeclaration = when(val d = routeType.declaration){
                is KSTypeAlias ->{
                    d.type.resolve().declaration as KSClassDeclaration
                }
                else -> {
                    d as KSClassDeclaration
                }
            }

            val composeNavParam = StringBuilder()
            val typeMapKv = mutableListOf<String>()

            //扫描路由类的参数，对标记了Serializable的参数类型生成type map
            val parameters = routeDeclaration.primaryConstructor?.parameters
            if (!parameters.isNullOrEmpty()) {
                parameters.forEach { parameter ->
                    val cType = parameter.type.resolve()
                    val declaration = cType.declaration
                    when (declaration.simpleName.asString()) {
                        "ByteArray" -> {
                            typeMapKv += "typeOf<ByteArray?>() to ByteArrayNavType"
                        }

                        "List" -> {
                            val listType = cType.arguments.first().type!!.resolve().declaration
                            if (listType.isSerializable())
                                typeMapKv += serializerOf(
                                    "List<${listType.qualifiedName?.asString()}>",
                                    "List<${listType.simpleName.asString()}>",
                                    cType.nullability == Nullability.NULLABLE
                                )
                        }

                        "Array" -> {
                            val arrayType = cType.arguments.first().type!!.resolve().declaration
                            if (arrayType.isSerializable())
                                typeMapKv += serializerOf(
                                    "Array<${arrayType.qualifiedName?.asString()}>",
                                    "Array<${arrayType.simpleName.asString()}>",
                                    cType.nullability == Nullability.NULLABLE
                                )
                        }

                        else -> {

                            if (declaration.isSerializable()) {
                                typeMapKv += serializerOf(
                                    declaration.qualifiedName!!.asString(),
                                    declaration.simpleName.asString(),
                                    cType.nullability == Nullability.NULLABLE
                                )
                            }/* else if (declaration.isEnum()){
                                typeMapKv += enumOf(
                                    declaration.qualifiedName!!.asString(),
                                    declaration.simpleName.asString(),
                                    cType.nullability == Nullability.NULLABLE
                                )
                            }*/
                        }
                    }
                }
            }

            if (typeMapKv.isNotEmpty()) {
                composeNavParam.append("\ntypeMap = hashMapOf(\n")
                typeMapKv.forEach {
                    composeNavParam.append(it).append(",\n")
                }
                composeNavParam.append("\n)")
            }

            logger.warn(typeMapKv.joinToString("\n") { it })

            data.beginControlFlow("composable<${routeType.canonicalName()}>($composeNavParam)")
                .addStatement("${function.qualifiedName?.asString()}($params)")
                .endControlFlow()
        }
    }

    private fun checkType(routeType: KSType, function: KSFunctionDeclaration) {
        val routeDeclaration = routeType.declaration
        val routeTypeName = routeDeclaration.simpleName.asString()

        if (!routeTypeName.startsWith(ROUTE)) {
            //如果没找到route开头的类，继续查找route开头的类型别名
            val ta = function.containingFile?.declarations
                ?.mapNotNull {
                    var alias: KSTypeAlias? = null
                    if (it.simpleName.asString().startsWith(ROUTE) && it is KSTypeAlias)
                        alias = it
                    alias
                }
                ?.firstOrNull()

            if (ta == null || ta.type.resolve().declaration.simpleName.asString() != routeTypeName)
                error("作为导航路由的类须使用Route名字开头，用于统一提示, ${function.qualifiedName?.asString()} ")
        }

        if (!routeDeclaration.isSerializable()) {
            //如果是类型别名则使用源类型判断
            if (routeDeclaration is KSTypeAlias){
                if (routeDeclaration.type.resolve().declaration.isSerializable())
                    return
            }

            if (!routeType.starProjection().declaration.isSerializable())
                error("路由类 ${routeType.canonicalName()} 需要@Serializable")
        }

    }


    /**
     * 当前声明是否是可序列化
     */
    private fun KSDeclaration.isSerializable() = annotations.any { it.shortName.asString() == "Serializable" }

    private fun KSDeclaration.isEnum() = if (this is KSClassDeclaration)
        getAllSuperTypes().any { it.declaration.simpleName.asString().startsWith("Enum") }
    else false

    private fun serializerOf(type: String, name: String, isNull: Boolean) =
        "typeOf<$type ${if (isNull) "?" else ""}>().let {\nit to KSerializerNavType(serializer(it),\"$name\",$isNull)\n}"

    private fun enumOf(type: String, name: String, isNull: Boolean) =
        "typeOf<${type}>() to EnumNavType(${type}.entries,\"$name\",$isNull)"

    override fun defaultHandler(node: KSNode, data: CodeBlock.Builder) {}


}

fun KSType.canonicalName(): String? {
    return declaration.qualifiedName?.asString()
}

//@OptIn(KspExperimental::class)
//inline fun <T : Annotation> T.asClassName(block: T.() -> KClass<*>): ClassName {
//    return try {
//        block(this).asClassName()
//    } catch (e: KSTypeNotPresentException) {
//        e.ksType.toClassName()
//    }
//}
//
//@OptIn(KspExperimental::class)
//inline fun <T : Annotation> T.asClassNames(block: T.() -> Array<KClass<*>>): List<ClassName> {
//    return try {
//        block(this).map { it.asClassName() }
//    } catch (e: KSTypesNotPresentException) {
//        e.ksTypes.map { it.toClassName() }
//    }
//}
