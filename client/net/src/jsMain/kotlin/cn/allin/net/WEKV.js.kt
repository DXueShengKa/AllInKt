package cn.allin.net

import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.reflect.KProperty


internal actual inline operator fun <reified T> WEKV.getValue(thisRef: Any?, property: KProperty<*>): Data<T> {
    val v = when (T::class) {
        Int::class -> 0
        Long::class -> 0L
        Float::class -> 0.0F
        Double::class -> 0.0
        Boolean::class -> false
        Set::class -> setOf<String>()
        else -> ""
    }
    return Data(property.name,v).unsafeCast<Data<T>>()
}

actual class Data<T>(private val key: String, val defaultValue: T) {
    private actual constructor() : this("", null!!)

    private var value: T? = null

    actual suspend fun set(v: T) {
        this.value = v
        localStorage[key] = JSON.stringify(v)
    }

    actual suspend fun setNotCache(v: T) {
        localStorage[key] = JSON.stringify(v)
    }

    actual suspend fun remove() {
        value = null
        localStorage.removeItem(key)
    }

    actual suspend fun get(): T {
        value?.let { return it }
        val v = JSON.parse<T>(localStorage[key] ?: return defaultValue)
        value = v
        return v
    }

    actual fun getOrNull(): T? {
        value?.let { return it }
        val v = JSON.parse<T>(localStorage[key] ?: return null)
        value = v
        return v
    }


    fun setNotStorage(v:T){
        this.value = v
    }

}
