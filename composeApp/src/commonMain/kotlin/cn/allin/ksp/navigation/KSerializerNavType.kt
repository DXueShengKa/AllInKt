package cn.allin.ksp.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import cn.allin.AllProtoBuf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlin.enums.EnumEntries


@OptIn(ExperimentalSerializationApi::class)
class KSerializerNavType<T : Any?>(
    private val serializer: KSerializer<T>,
    override val name: String,
    override val isNullableAllowed: Boolean
) : NavType<T>(isNullableAllowed) {

    override fun get(bundle: SavedState, key: String): T? {
        return bundle.read {
            val ba = getString(key).encodeToByteArray()
            AllProtoBuf.decodeFromByteArray(serializer, ba)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun parseValue(value: String): T {
        val tmp: T = t as T
        t = null
        return tmp
    }

    private var t: T? = null

    override fun serializeAsValue(value: T): String {
        t = value
        return name
    }

    override fun put(bundle: SavedState, key: String, value: T) {

        bundle.write {
            putString(key, AllProtoBuf.encodeToByteArray(serializer, value).decodeToString())
        }
    }
}

val ByteArrayNavType = object : NavType<ByteArray?>(true) {
    override val name: String
        get() = "byte[]"


    override fun get(bundle: SavedState, key: String): ByteArray {
        return bundle.read {
            getString(key).encodeToByteArray()
        }
    }

    private var wr: ByteArray? = null

    override fun parseValue(value: String): ByteArray? {
        val ba = wr
        wr = null
        return ba
    }

    override fun serializeAsValue(value: ByteArray?): String {
        if (value == null) return ""
        wr = value
        return name
    }

    override fun valueEquals(value: ByteArray?, other: ByteArray?): Boolean {
        return value.contentEquals(other)
    }

    override fun put(bundle: SavedState, key: String, value: ByteArray?) {
        value ?: return
        bundle.write {
            putString(key, value.decodeToString())
        }
    }

}


class EnumNavType<T : Enum<T>>(
    private val enumEntries: EnumEntries<T>,
    override val name: String,
    override val isNullableAllowed: Boolean
) : NavType<T>(isNullableAllowed) {


    override fun get(bundle: SavedState, key: String): T? {
        return bundle.read {
            getIntOrNull(key)?.let { enumEntries[it] }
        }
    }

    override fun parseValue(value: String): T {
        return enumEntries[value.toInt()]
    }

    override fun serializeAsValue(value: T): String {
        return value.ordinal.toString()
    }

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write {
            putInt(key,value.ordinal)
        }
    }
}
