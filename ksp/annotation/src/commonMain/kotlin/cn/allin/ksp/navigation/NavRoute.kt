package cn.allin.ksp.navigation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class NavRoute(
    val routeType: KClass<*> = String::class,
    val routeString: String = "",
)
