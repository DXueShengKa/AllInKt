package cn.allin.ksp.server

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class EntityToVo(
    val vo: Array<KClass<*>>
)
