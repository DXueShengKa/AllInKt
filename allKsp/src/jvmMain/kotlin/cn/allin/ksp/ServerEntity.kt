package cn.allin.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.squareup.kotlinpoet.ksp.toTypeName

@OptIn(KspExperimental::class)
fun genVo(resolver: Resolver, logger: KSPLogger) {

    resolver.getDeclarationsFromPackage("cn.allin.exposed.table")
        .mapNotNull { ksDeclaration ->
            logger.warn(ksDeclaration.simpleName.asString())
            val classDeclaration = ksDeclaration.closestClassDeclaration()
            if (classDeclaration?.superTypes?.any {
                it.resolve().declaration.simpleName.asString().endsWith("IdTable") } == true) {
                classDeclaration
            } else {
                null
            }
        }
        .forEach { d ->
            d.getDeclaredProperties()
                .forEach {
                    val t = it.type.resolve().arguments.first().type!!.toTypeName()
                    logger.warn("${it.simpleName.asString()} ")
                }
            logger.warn(d.simpleName.asString())
        }

    error("")

}