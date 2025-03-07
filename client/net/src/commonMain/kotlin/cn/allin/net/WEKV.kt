package cn.allin.net

import kotlin.reflect.KProperty


expect class Data<T> private constructor () {

    suspend fun set(v: T)

    suspend fun setNotCache(v: T)

    suspend fun remove()

    suspend fun get(): T

    fun getOrNull(): T?

}

internal expect inline operator fun <reified T> WEKV.getValue(thisRef: Any?, property: KProperty<*>): Data<T>


object WEKV {
    val authorization: Data<String> by WEKV

}
