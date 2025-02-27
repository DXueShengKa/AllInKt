package cn.allin.net

import cn.allin.ValidatorError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf

val ContentTypeXProtobuf = ContentType("application", "x-protobuf")

const val SERVER_BASE_URL = "http://localhost:8020"


@OptIn(ExperimentalSerializationApi::class)
fun HttpClientConfig<*>.contentConverter() {
    HttpResponseValidator {
        validateResponse {
            if (it.status == HttpStatusCode.BadRequest) {
                throw ValidatorError(it.body())
            }
        }
    }

    install(ContentNegotiation) {
        register(ContentType.Application.Json, KotlinxSerializationConverter(cn.allin.InJson))
        register(ContentTypeXProtobuf, KotlinxSerializationConverter(ProtoBuf))
    }
}
