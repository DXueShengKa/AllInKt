@file:OptIn(ExperimentalJsStatic::class)

package cn.allin.net

import cn.allin.ui.AddUser
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*


var HeaderAuthorization: String? = null


private val http = HttpClient(Js) {
    defaultRequest {
        url(SERVER_BASE_URL)
        if (contentType() == null)
            contentType(ContentType.Application.Json)
//            contentType(ContentTypeXProtobuf)

        header(HttpHeaders.Authorization, HeaderAuthorization)
    }

    contentConverter()
}

object ReqUser {

    @JsStatic
    suspend fun getUserAll(): List<UserVO> {
        return http.get("user").body()
    }

    @JsStatic
    suspend fun addUser(addUser: AddUser) {
        http.post("user") {
            setBody(JSON.stringify(addUser))
        }.body<Unit>()
    }
}


object ReqAuth {

    @JsStatic
    suspend fun auth(baseVO: UserVO): MsgVO<String> {
        val response = http.post("auth") {
            setBody(baseVO)
        }.call.response

        val msgVO = response.body<MsgVO<String>>()

        if (msgVO.code == MsgVO.OK){

            HeaderAuthorization = msgVO.data

            response.headers.forEach { s, strings ->
                println("$s $strings")
            }
            console.log(response.headers)
        }

        return msgVO
    }

}
