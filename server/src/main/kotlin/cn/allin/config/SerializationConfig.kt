package cn.allin.config

import cn.allin.InJson
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.http.converter.protobuf.KotlinSerializationProtobufHttpMessageConverter

@Configuration
class SerializationConfig {

    @Bean
    fun jsonMessageConverter(): HttpMessageConverter<*> {
        return KotlinSerializationJsonHttpMessageConverter(
            InJson
        )
    }

    
    @OptIn(ExperimentalSerializationApi::class)
    @Bean
    fun protobufMessageConverter(): HttpMessageConverter<*> {
        return KotlinSerializationProtobufHttpMessageConverter(
            ProtoBuf.Default
        )
    }

}