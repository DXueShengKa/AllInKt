
package cn.allin.net

import cn.allin.BuildConfig
import cn.allin.ValidatorError
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.serialization
import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.ExperimentalSerializationApi

val ContentTypeXProtobuf = ContentType("application", "x-protobuf")

const val SERVER_BASE_URL = "http://localhost:8020"

// const val SERVER_BASE_URL = "http://www.839421549.xyz/server"

expect val ktorEngineFactory: HttpClientEngineFactory<*>

@OptIn(ExperimentalSerializationApi::class)
fun HttpClientConfig<*>.contentNegotiation() {
    install(ContentNegotiation) {
        serialization(ContentTypeXProtobuf, cn.allin.AllProtoBuf)
        serialization(ContentType.Application.Json, cn.allin.AllJson)
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun HttpClientConfig<*>.commonConfig() {
    Charsets {
        register(Charsets.UTF_8)
    }

    HttpResponseValidator {
        validateResponse {
            when (it.status) {
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
        WEKV.authorization.getOrNull()?.also {
            header(HttpHeaders.Authorization, it)
        }
        accept(ContentType.Application.Json)
        contentType(ContentType.Application.Json)

//        if (contentType() == null)
//            contentType(ContentType.Application.Json)
    }

    install(ContentNegotiation) {
        serialization(ContentTypeXProtobuf, cn.allin.AllProtoBuf)
        serialization(ContentType.Application.Json, cn.allin.AllJson)
    }

    if (BuildConfig.DEBUG) {
        Logging {
            this.level = LogLevel.ALL
            this.logger = Logger.DEFAULT
        }
    }
}
