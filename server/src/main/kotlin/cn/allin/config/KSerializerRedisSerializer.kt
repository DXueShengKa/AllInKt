package cn.allin.config

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import org.springframework.data.redis.serializer.RedisSerializer

inline fun <reified T : Any> kotlinRedisSerializer(binaryFormat: BinaryFormat) = KSerializerRedisSerializer(binaryFormat, T::class.java, serializer<T>())

@OptIn(InternalSerializationApi::class)
class KSerializerRedisSerializer<T : Any>(
    private val binaryFormat: BinaryFormat,
    private val clazz: Class<T>,
    private val serializer: KSerializer<T>,
) : RedisSerializer<T> {
    override fun serialize(value: T?): ByteArray {
        return binaryFormat.encodeToByteArray(serializer, value ?: return ByteArray(0))
    }

    override fun deserialize(bytes: ByteArray?): T? {
        return binaryFormat.decodeFromByteArray(serializer, bytes ?: return null)
    }

    override fun canSerialize(type: Class<*>): Boolean = serializer == type.kotlin.serializerOrNull()

    override fun getTargetType(): Class<*> = clazz
}
