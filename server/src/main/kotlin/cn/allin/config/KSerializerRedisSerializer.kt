package cn.allin.config

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializerOrNull
import org.springframework.data.redis.serializer.RedisSerializer


@OptIn(InternalSerializationApi::class)
class KSerializerRedisSerializer<T>(private val binaryFormat: BinaryFormat) : RedisSerializer<T> {
    private var serializer: KSerializer<T>? = null

    override fun serialize(value: T?): ByteArray? {
        return binaryFormat.encodeToByteArray(serializer ?: return null, value ?: return null)
    }

    override fun deserialize(bytes: ByteArray?): T? {
        return binaryFormat.decodeFromByteArray(serializer ?: return null, bytes ?: return null)
    }

    override fun canSerialize(type: Class<*>): Boolean {
        serializer = type.kotlin.serializerOrNull() as? KSerializer<T>
        return serializer != null
    }

}
