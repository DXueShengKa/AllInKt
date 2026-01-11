package cn.allin.config

import cn.allin.AllJson
import cn.allin.AllProtoBuf
import kotlinx.serialization.ExperimentalSerializationApi
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.http.codec.protobuf.KotlinSerializationProtobufDecoder
import org.springframework.http.codec.protobuf.KotlinSerializationProtobufEncoder
import org.springframework.validation.Validator
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class AllInWebConfig : WebFluxConfigurer {
    @OptIn(ExperimentalSerializationApi::class)
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        val codecs: ServerCodecConfigurer.ServerDefaultCodecs = configurer.defaultCodecs()
        codecs.kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(AllJson))
        codecs.kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(AllJson))

        codecs.kotlinSerializationProtobufDecoder(KotlinSerializationProtobufDecoder(AllProtoBuf as kotlinx.serialization.protobuf.ProtoBuf))
        codecs.kotlinSerializationProtobufEncoder(KotlinSerializationProtobufEncoder(AllProtoBuf as kotlinx.serialization.protobuf.ProtoBuf))
    }

    override fun getValidator(): Validator? = SerializableValidator()
}
