package cn.allin.config

import cn.allin.utils.UserAuthenticationToken
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.web.reactive.function.client.WebClient

@OptIn(ExperimentalSerializationApi::class)
@Configuration
@EnableCaching
class CacheConfig {

    companion object {
        const val AUTH = "auth"
    }

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .build()
    }

    @Bean
    fun reactiveRedisTemplateByte(redisConnectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, ByteArray> {
        val context = RedisSerializationContext.newSerializationContext<String, ByteArray>()
            .key(StringRedisSerializer.UTF_8)
            .value(RedisSerializer.byteArray())
            .hashKey(StringRedisSerializer.UTF_8)
            .hashValue(RedisSerializer.byteArray())
            .build()

        val template = ReactiveRedisTemplate<String, ByteArray>(redisConnectionFactory,context)
        return template
    }

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        val config = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(kotlinRedisSerializer<UserAuthenticationToken>(ProtoBuf)))
//            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer<Any>(RedisSerializer.java()))
            .disableCachingNullValues()

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(config)
            .build()
    }

}
