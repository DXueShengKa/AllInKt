package cn.allin.service

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
@Service
class RedisService(
    private val reactiveRedisTemplateByte: ReactiveRedisTemplate<String, ByteArray>,
) {
    fun <T : Any> get(
        key: String,
        clazz: KClass<T>,
    ): Mono<T> =
        reactiveRedisTemplateByte
            .opsForValue()
            .get(key)
            .map {
                ProtoBuf.decodeFromByteArray(clazz.serializer(), it)
            }

    fun <T : Any> set(
        key: String,
        value: T,
        clazz: KClass<T>,
    ): Mono<Boolean> =
        reactiveRedisTemplateByte.opsForValue().set(
            key,
            ProtoBuf.encodeToByteArray(clazz.serializer(), value),
        )

    fun <T : Any> set(
        key: String,
        value: T,
        seconds: Long,
        clazz: KClass<T>,
    ): Mono<Boolean> =
        reactiveRedisTemplateByte.opsForValue().set(
            key,
            ProtoBuf.encodeToByteArray(clazz.serializer(), value),
            Duration.ofSeconds(seconds),
        )

    fun get(key: String): Mono<String> = reactiveRedisTemplateByte.opsForValue().get(key).map { String(it) }

    fun set(
        key: String,
        value: String,
    ): Mono<Boolean> =
        reactiveRedisTemplateByte.opsForValue().set(
            key,
            value.toByteArray(),
        )

    fun set(
        key: String,
        value: String,
        seconds: Long,
    ): Mono<Boolean> =
        reactiveRedisTemplateByte
            .opsForValue()
            .set(key, value.toByteArray(), Duration.ofSeconds(seconds))

    fun delete(key: String): Mono<Boolean> = reactiveRedisTemplateByte.opsForValue().delete(key)
}

inline fun <reified T : Any> RedisService.get(key: String): Mono<T> = get(key, T::class)

inline fun <reified T : Any> RedisService.set(
    key: String,
    value: T,
): Mono<Boolean> = set(key, value, T::class)
