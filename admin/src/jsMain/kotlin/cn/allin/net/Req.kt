@file:OptIn(ExperimentalJsStatic::class)

package cn.allin.net

import arrow.core.Either
import cn.allin.ServerParams
import cn.allin.ValidatorError
import cn.allin.apiRoute
import cn.allin.ui.PageParams
import cn.allin.vo.MsgVO
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import cn.allin.vo.QandaVO
import cn.allin.vo.RegionVO
import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import js.objects.jso
import js.typedarrays.Uint8Array
import js.typedarrays.toByteArray
import toolpad.core.UserSession
import web.file.File


object Req {

    private var _authHeader: String? = null


    suspend fun authToken(authHeader: String?, remember: Boolean = false) {
        if (authHeader == null) {
            WEKV.authorization.remove()
        } else if (remember) {
            WEKV.authorization.set(authHeader)
        }
        this._authHeader = authHeader
    }

    fun authToken(): String? {
        return _authHeader ?: WEKV.authorization.getOrNull()?.also {
            _authHeader = it
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
                authToken()?.also {
                    append(HttpHeaders.Authorization, it)
                }
            }
        }

        contentNegotiation()

        if (false) Logging {
            this.level = LogLevel.ALL
            this.logger = Logger.DEFAULT
        }
    }
}

suspend fun Req.getUserPage(pageParams: PageParams?): PageVO<UserVO> {
    val response = http.get(apiRoute.user.page) {
        parameter(ServerParams.PAGE_SIZE, pageParams?.size)
        parameter(ServerParams.PAGE_INDEX, pageParams?.index)
    }
    return response.body()
}


suspend fun Req.deleteUser(ids: List<Long>): Boolean {
    return http.delete(apiRoute.user.path) {
        parameter("ids", ids.joinToString())
    }.body()
}

suspend fun Req.addUser(addUser: UserVO) {
    http.post(apiRoute.user.path) {
        setBody(addUser)
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

suspend fun Req.currentUser(): UserSession? {
    val user: UserVO? = http.get(apiRoute.user.path).body()
    return if (user != null) {
        jso {
            id = user.id.toString()
            name = user.name
            email = user.email
        }
    } else null
}

suspend fun Req.deleteAuth() {
    val msgVO: MsgVO<String> = http.delete(apiRoute.auth.path).body()

    if (msgVO.message == MsgVO.success) {
        authToken(null)
    }

}


suspend fun Req.regionProvince(): List<RegionVO> {
    return http.get(apiRoute.region.province.path).body()
}

suspend fun Req.regionCity(provinceId: Int): List<RegionVO> {
    return http.get(apiRoute.region.city.path(provinceId)).body()
}

suspend fun Req.regionCounty(cityId: Int): List<RegionVO> {
    return http.get(apiRoute.region.country.path(cityId)).body()
}


suspend fun Req.getQandaPage(pageParams: PageParams?): PageVO<QandaVO> {
    val response = http.get(apiRoute.qanda.page) {
        parameter(ServerParams.PAGE_SIZE, pageParams?.size)
        parameter(ServerParams.PAGE_INDEX, pageParams?.index)
    }
    return response.body()
}

suspend fun Req.addQanda(vo: QandaVO): MsgVO<Int> {
    val response = http.post(apiRoute.qanda.path) {
        setBody(vo)
    }
    return response.body()
}

suspend fun Req.deleteQanda(id: Int): Either<String, Unit> {
    val response = http.delete(apiRoute.qanda.path(id))
    return response.body()
}

suspend fun Req.deleteQanda(ids: List<Int>): MsgVO<Int> {
    val response = http.delete(apiRoute.qanda.path){
        parameter("ids", ids.joinToString())
    }
    return response.body()
}

suspend fun Req.getQaTagPage(pageParams: PageParams?): PageVO<QaTagVO> {
    return http.get(apiRoute.qanda.tag.page.path) {
        parameter(ServerParams.PAGE_SIZE, pageParams?.size)
        parameter(ServerParams.PAGE_INDEX, pageParams?.index)
    }.body()
}

suspend fun Req.uploadExcel(file: File): Boolean {
    val bytes = file.arrayBuffer()

    val msg: MsgVO<String> = http.submitFormWithBinaryData(apiRoute.qanda.excel.path, formData {
        append("file", file.name) {
            val ba = Uint8Array(bytes).toByteArray()
            write(ba, 0, ba.size)
        }
    }).body()

    return msg.isSuccess
}
