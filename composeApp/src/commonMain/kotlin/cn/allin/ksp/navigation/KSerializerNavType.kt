package cn.allin.ksp.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
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


    override fun get(bundle: Bundle, key: String): T? {
        val ba = bundle.getByteArray(key) ?: return null
        return AllProtoBuf.decodeFromByteArray(serializer, ba)
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

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putByteArray(key, AllProtoBuf.encodeToByteArray(serializer, value))
    }
}

val ByteArrayNavType = object : NavType<ByteArray?>(true) {
    override val name: String
        get() = "byte[]"


    override fun get(bundle: Bundle, key: String): ByteArray? {
        return bundle.getByteArray(key)
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

    override fun put(bundle: Bundle, key: String, value: ByteArray?) {
        bundle.putByteArray(key, value)
    }

}


class EnumNavType<T : Enum<T>>(
    private val enumEntries: EnumEntries<T>,
    override val name: String,
    override val isNullableAllowed: Boolean
) : NavType<T>(isNullableAllowed) {


    override fun get(bundle: Bundle, key: String): T? {
        val i = bundle.getInt(key, -1)
        return if (i > -1) enumEntries[i] else null
    }

    override fun parseValue(value: String): T {
        return enumEntries[value.toInt()]
    }

    override fun serializeAsValue(value: T): String {
        return value.ordinal.toString()
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putInt(key,value.ordinal)
    }
}
