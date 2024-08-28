package cn.allin.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

class CodeProcessor (environment: SymbolProcessorEnvironment): SymbolProcessor {
    private val codeGenerator = environment.codeGenerator
    private val logger = environment.logger
    private val options = environment.options

    private var invoked = false


    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) return emptyList()
        invoked = true


        val c = """
            
    fun a(){

        println("sssssss")
    }
        """.trimIndent()
        codeGenerator.createNewFile(Dependencies.ALL_FILES,"cn.allin","ksp").bufferedWriter().use {
            it.write(c)
        }
        return emptyList()
    }
}