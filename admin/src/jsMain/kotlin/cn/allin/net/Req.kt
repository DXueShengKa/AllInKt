@file:OptIn(ExperimentalJsStatic::class)

package cn.allin.net

import cn.allin.ValidatorError
import cn.allin.api.ApiQanda
import cn.allin.api.ApiUser
import cn.allin.apiRoute
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import js.objects.jso
import js.typedarrays.Uint8Array
import js.typedarrays.toByteArray
import toolpad.core.UserSession
import web.file.File


object Req {


    suspend fun authToken(authHeader: String?, remember: Boolean = false) {
        if (authHeader == null) {
            WEKV.authorization.remove()
        } else if (remember) {
            WEKV.authorization.set(authHeader)
        } else {
            WEKV.authorization.setNotStorage(authHeader)
        }
    }


    val http = HttpClient(Js) {
//        commonConfig()
        HttpResponseValidator {
            validateResponse {
                when (it.status) {
                    HttpStatusCode.Unauthorized -> {
                        authToken(null)
                    }

                    HttpStatusCode.BadRequest -> {
                        throw ValidatorError(it.body())
                    }
                }
            }
        }

        defaultRequest {
            url(SERVER_BASE_URL)
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
//            accept(ContentTypeXProtobuf)
//            contentType(ContentTypeXProtobuf)
            headers {
                WEKV.authorization.getOrNull()?.also {
                    append(HttpHeaders.Authorization, it)
                }
            }
        }

        contentNegotiation()
    }
}



suspend fun Req.auth(baseVO: UserVO, remember: Boolean): MsgVO<String> {
    val response = http.post(apiRoute.auth.path) {
        setBody(baseVO)
    }.call.response

    val msgVO = response.body<MsgVO<String>>()

    if (msgVO.message == MsgVO.success) {
        authToken(msgVO.data, remember)
    }

    return msgVO
}


suspend fun Req.deleteAuth() {
    val msgVO: MsgVO<String> = http.delete(apiRoute.auth.path).body()

    if (msgVO.message == MsgVO.success) {
        authToken(null)
    }

}

suspend fun ApiUser.userSession(): UserSession? {
    val user: UserVO? = get()
    return if (user != null) {
        jso {
            id = user.id.toString()
            name = user.name
            email = user.email
        }
    } else null
}



suspend fun Req.uploadExcel(file: File): Boolean {
    val bytes = file.arrayBuffer()

    val msg: MsgVO<String> = http.submitFormWithBinaryData(ApiQanda.pathExcel, formData {
        append("file", file.name) {
            val ba = Uint8Array(bytes).toByteArray()
            write(ba, 0, ba.size)
        }
    }).body()

    return msg.isSuccess
}
