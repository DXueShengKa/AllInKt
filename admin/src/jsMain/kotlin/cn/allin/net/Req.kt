@file:OptIn(ExperimentalJsStatic::class)

package cn.allin.net

import cn.allin.ServerRoute
import cn.allin.ui.AddUser
import cn.allin.ui.RouteAuth
import cn.allin.utils.lastDayOfMonth
import cn.allin.utils.toLocalDate
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.localStorage
import kotlinx.datetime.LocalDate
import org.w3c.dom.get
import org.w3c.dom.set


var HeaderAuthorization: String? = localStorage[RouteAuth]


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
        return http.get(ServerRoute.USER).body()
    }

    @JsStatic
    suspend fun addUser(addUser: AddUser) {
        http.post(ServerRoute.USER) {
            LocalDate.parse("").lastDayOfMonth()
            setBody(UserVO(
                name = addUser.name,
                password = addUser.password,
                birthday = addUser.birthday.toLocalDate()
            ))
        }.body<Unit>()
    }
}


object ReqAuth {

    @JsStatic
    suspend fun auth(baseVO: UserVO): MsgVO<String> {
        val response = http.post(ServerRoute.AUTH) {
            setBody(baseVO)
        }.call.response

        val msgVO = response.body<MsgVO<String>>()

        if (msgVO.code == MsgVO.OK){

            HeaderAuthorization = msgVO.data
            localStorage[RouteAuth] = msgVO.data!!

            response.headers.forEach { s, strings ->
                println("$s $strings")
            }
            console.log(response.headers)
        }

        return msgVO
    }

}
