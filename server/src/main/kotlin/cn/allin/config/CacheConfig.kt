package cn.allin.config

import cn.allin.utils.UserAuthenticationToken
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import org.springframework.beans.factory.annotation.Value
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
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

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

        val template = ReactiveRedisTemplate<String, ByteArray>(redisConnectionFactory, context)
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


    @Value("\${aws.endpoint}")
    private lateinit var endpoint: String

    @Value("\${aws.accessKey}")
    private lateinit var accessKey: String

    @Value("\${aws.secretKey}")
    private lateinit var secretKey: String

    @Value("\${aws.region}")
    private lateinit var region: String


    @Bean
    fun s3Client(): S3AsyncClient {
        return S3AsyncClient.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .region(Region.of(region))
            .build()
    }


    @Bean
    fun s3PreSigner(): S3Presigner {
        return S3Presigner.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .region(Region.of(region))
            .build()
    }

}
