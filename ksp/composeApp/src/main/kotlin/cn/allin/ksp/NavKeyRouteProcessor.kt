package cn.allin.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import java.io.OutputStreamWriter

class NavKeyRouteProcessor(
    environment: SymbolProcessorEnvironment,
) : SymbolProcessor {
    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger

    init {
        environment.options
    }

    private val navKeyName = "androidx.navigation3.runtime.NavKey"
    private val baseRouteName = "com.logic.navigation.NavRoute"

    private var hasGenerated = false

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (hasGenerated) {
            return emptyList()
        }

        val routeList = mutableListOf<RouteMetadata>()
        val routeFile = mutableListOf<KSFile>()
        resolver
            .getAllFiles()
            .flatMap { it.declarations }
            // 只扫com.atbot包下
            .filter { it.packageName.asString().startsWith("com.atbot") }
            .filterIsInstance<KSClassDeclaration>()
            .filter {
                // 查找BaseRoute类
                it.superTypes.any { typeReference ->
                    typeReference
                        .resolve()
                        .declaration.qualifiedName
                        ?.asString() == baseRouteName
                }
            }.forEach { routeKS ->
                // 查找nav key子类
                var navKeyKs: KSClassDeclaration? = null
                var clazzContentKey = false
                var metadata = false

                routeKS.declarations
                    .filterIsInstance<KSClassDeclaration>()
                    .forEach { d ->
                        if (d.superTypes.any {
                                it
                                    .resolve()
                                    .declaration.qualifiedName
                                    ?.asString() == navKeyName
                            }
                        ) {
                            navKeyKs = d
                        }
                        if (d.isCompanionObject) {
                            // 在伴生对象里找 metadata clazzContentKey
                            d.getAllProperties().forEach {
                                when (it.simpleName.asString()) {
                                    RouteMetadata.metadata -> {
                                        metadata = true
                                    }

                                    RouteMetadata.clazzContentKey -> {
                                        clazzContentKey = true
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }

                routeList.add(
                    RouteMetadata(
                        key = navKeyKs!!.qualifiedName!!.asString(),
                        route = routeKS.qualifiedName!!.asString(),
                        clazzContentKey,
                        metadata,
                    ),
                )

                routeKS.containingFile?.let(routeFile::add)
            }

        if (routeList.isNotEmpty()) {
            generateNavKeyRoute(
                routeFile.toTypedArray(),
                routeList,
            )
            hasGenerated = true
        }

        return emptyList()
    }

    private fun generateNavKeyRoute(
        orgFiles: Array<KSFile>,
        navKeyList: List<RouteMetadata>,
    ) {
        val packageName = "com.atbot.ui"
        val fileName = "RouteNavKey"

        val file =
            codeGenerator.createNewFile(
                dependencies = Dependencies(aggregating = true, sources = orgFiles),
                packageName = packageName,
                fileName = fileName,
            )

        OutputStreamWriter(file).use { writer ->
            writer.write(
                """
                package $packageName

                import androidx.navigation3.runtime.NavEntry
                import androidx.navigation3.runtime.NavKey
                import androidx.navigation3.runtime.entryProvider
                import androidx.savedstate.serialization.SavedStateConfiguration
                import com.logic.ui.entryRoute
                import kotlinx.serialization.modules.SerializersModule
                import kotlinx.serialization.modules.polymorphic
                import kotlinx.serialization.modules.subclass

                val AtRouteCong = SavedStateConfiguration {
                    serializersModule = SerializersModule {
                        polymorphic(NavKey::class){
                            logicSubclass()

                """.trimIndent(),
            )

            navKeyList.forEach {
                writer.write("\t\t\tsubclass(${it.key}::class)\n")
            }

            writer.write(
                """
                        }
                    }
                }

                val AtRouteNavEntry: (key: NavKey) -> NavEntry<NavKey> = entryProvider {
                    logicEntryRoute()

                """.trimIndent(),
            )

            navKeyList.forEach {
                writer.write("\tentryRoute<${it.key}>(")
                if (it.metadata) {
                    writer.write("metadata = ${it.route}.metadata, ")
                }
                if (it.clazzContentKey) {
                    writer.write("clazzContentKey = ${it.route}.clazzContentKey, ")
                }
                writer.write(") { ${it.route}() }\n")
            }

            writer.write("}")
        }
    }

    class RouteMetadata(
        val key: String,
        val route: String,
        val clazzContentKey: Boolean,
        val metadata: Boolean,
    ) {
        companion object {
            const val clazzContentKey = "clazzContentKey"
            const val metadata = "metadata"
        }
    }
}
