package cn.allin.ksp

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType

/**
 * 查找子类
 */
fun Sequence<KSFile>.getSubclassesByType(
    type: KSType,
): Sequence<KSClassDeclaration> = flatMap { ksFile ->
    //从文件里包含的声明中找出类
    ksFile.declarations.mapNotNull { it.closestClassDeclaration() }
}.filter { classDeclaration ->
    classDeclaration.superTypes.any { it.resolve() == type }
}


fun Sequence<KSFile>.filterSubclassesByType(
    type: KSType,
): Sequence<KSFile> = filter { ksFile ->
    ksFile.declarations
        .flatMap { it.closestClassDeclaration()?.superTypes ?: emptySequence() }
        .any { it.resolve() == type }
}