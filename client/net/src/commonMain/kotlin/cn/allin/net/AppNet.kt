package cn.allin.net

import cn.allin.BuildConfig
import cn.allin.ValidatorError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.utils.io.charsets.*
import kotlinx.serialization.ExperimentalSerializationApi

val ContentTypeXProtobuf = ContentType("application", "x-protobuf")

const val SERVER_BASE_URL = "http://localhost:8020"

//const val SERVER_BASE_URL = "http://www.839421549.xyz/server"

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
