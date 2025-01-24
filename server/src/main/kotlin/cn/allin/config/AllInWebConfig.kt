package cn.allin.config

import cn.allin.InJson
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.http.codec.protobuf.KotlinSerializationProtobufDecoder
import org.springframework.http.codec.protobuf.KotlinSerializationProtobufEncoder
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class AllInWebConfig: WebFluxConfigurer {

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        val codecs: ServerCodecConfigurer.ServerDefaultCodecs = configurer.defaultCodecs()
        codecs.kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(InJson))
        codecs.kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(InJson))


        codecs.kotlinSerializationProtobufDecoder(KotlinSerializationProtobufDecoder())
        codecs.kotlinSerializationProtobufEncoder(KotlinSerializationProtobufEncoder())
    }

}