package cn.allin.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class AllInProcessorProvider : SymbolProcessorProvider {
    companion object {
        const val MODULE_SERVER = "server"
        const val MODULE_COMPOSE_APP = "composeApp"
        const val MODULE_SHARED = "shared"
    }

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return CodeProcessor(environment)
    }
}