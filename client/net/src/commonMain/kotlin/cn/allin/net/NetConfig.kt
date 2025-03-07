package cn.allin.net

import cn.allin.ValidatorError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf

val ContentTypeXProtobuf = ContentType("application", "x-protobuf")

const val SERVER_BASE_URL = "http://localhost:8020"

expect val ktorEngineFactory: HttpClientEngineFactory<*>

@OptIn(ExperimentalSerializationApi::class)
fun HttpClientConfig<*>.commonConfig() {
    HttpResponseValidator {
        validateResponse {
            when(it.status) {
                HttpStatusCode.Unauthorized -> {
                    WEKV.authorization.remove()
                }
                HttpStatusCode.BadRequest -> {
                    throw ValidatorError(it.body())
                }
            }
        }
    }

    defaultRequest {
        url(SERVER_BASE_URL)
        headers {
            WEKV.authorization.getOrNull()?.also { append(HttpHeaders.Authorization, it) }
        }
        if (contentType() == null)
            contentType(ContentType.Application.Json)
    }

    install(ContentNegotiation) {
        serialization(ContentTypeXProtobuf, ProtoBuf)
        serialization(ContentType.Application.Json, cn.allin.InJson)
    }
}
