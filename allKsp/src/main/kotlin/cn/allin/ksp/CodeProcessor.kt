package cn.allin.ksp

import cn.allin.ksp.navigation.GenerateNav
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

class CodeProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger
    private val options = environment.options

    private var invoked = false


    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) return emptyList()
        invoked = true
        val moduleName = resolver.getModuleName().asString()
        logger.warn(moduleName)

        when {
            moduleName.contains(AllInProcessorProvider.MODULE_SERVER) -> {
                generatorEntityToVo(resolver,codeGenerator,logger)
            }

            moduleName.contains(AllInProcessorProvider.MODULE_COMPOSE_APP) -> {
                val nav = GenerateNav(codeGenerator, resolver, logger, "App")
                nav.generateNavGraphDsl()
                nav.writeFile()
            }

            moduleName.contains(AllInProcessorProvider.MODULE_SHARED) -> {
                generatorSerializationField(resolver, codeGenerator, logger)
                generateBuildConfig(codeGenerator,options, moduleName)
            }
        }

        return emptyList()
    }
}
