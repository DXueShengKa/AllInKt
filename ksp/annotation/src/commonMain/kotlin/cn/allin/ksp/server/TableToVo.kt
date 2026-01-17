package cn.allin.ksp.server

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class TableToVo(
    val vo: Array<KClass<*>>
)
