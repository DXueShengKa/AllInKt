package cn.allin.net

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty


@Suppress("UNCHECKED_CAST")
internal actual inline operator fun <reified T> WEKV.getValue(thisRef: Any?, property: KProperty<*>): Data<T> {
    val d = when (T::class) {
        Int::class -> Data(intPreferencesKey(property.name), 0)
        Long::class -> Data(longPreferencesKey(property.name), 0L)
        Float::class -> Data(floatPreferencesKey(property.name), 0.0f)
        Double::class -> Data(doublePreferencesKey(property.name), 0.0)
        Boolean::class -> Data(booleanPreferencesKey(property.name), false)
        Set::class -> Data(stringSetPreferencesKey(property.name), setOf())
        else -> Data(stringPreferencesKey(property.name), "")
    }
    return d as Data<T>
}

internal lateinit var weKv: DataStore<Preferences>

actual class Data<T>(
    private val key: Preferences.Key<T>,
    private val defaultValue: T,
) {
    private actual constructor() : this(null!!, null!!)

    private var value: T? = null

    actual suspend fun set(v: T) {
        this.value = v
        weKv.edit {
            it[key] = v
        }
    }

    actual suspend fun setNotCache(v: T) {
        weKv.edit {
            it[key] = v
        }
    }

    actual suspend fun remove() {
        this.value = null
        weKv.edit {
            it.remove(key)
        }
    }

    actual suspend fun get(): T {
        return value ?: (weKv.data.first()[key] ?: defaultValue).also { value = it }
    }

    actual fun getOrNull(): T? {
        return value ?: runBlocking { weKv.data.first()[key] }.also { value = it }
    }
}

expect fun WEKV.initialize(context: Any?)
