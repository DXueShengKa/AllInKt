package cn.allin.net

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

const val APP_API_URL:String = SERVER_BASE_URL


fun <T : HttpClientEngineConfig> HttpClientConfig<T>.appCommonConfig() {

//    HttpResponseValidator {
//        handleResponseExceptionWithRequest { cause, _ ->
//            handleThrowable(cause)
//        }
//    }
//
//    ResponseObserver {
//        when (it.status) {
//            HttpStatusCode.Unauthorized -> {
//                WEKV.token.remove()
//            }
//
//            HttpStatusCode.OK -> {
//                onSuccessfulResponse(it.request.url.pathSegments, it.body())
//            }
//        }
//    }

    defaultRequest {
        url(APP_API_URL)
        headers {
            HttpHeaders.Authorization
//            WEKV.token.get().takeIf { it.isNotEmpty() }?.let {
//                append(API_HEADER_TOKEN, it)
//            }
//            append(API_HEADER_LANG, API_DEFAULT_LANG)
        }
        if (contentType() == null)
            contentType(ContentTypeXProtobuf)

//        userAgent("Android")
    }

//    if (BuildConfig.TEST)
//        install(Logging, ktorLogger())

//    install(ContentNegotiation) {
//        register(ContentType.Application.Json, WeJsonConverter())
//    }
}
