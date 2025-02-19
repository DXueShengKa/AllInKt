@file:OptIn(ExperimentalJsStatic::class)

package cn.allin.net

import cn.allin.ServerParams
import cn.allin.ServerRoute
import cn.allin.ui.RouteAuth
import cn.allin.vo.MsgVO
import cn.allin.vo.PageVO
import cn.allin.vo.RegionVO
import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.localStorage
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

data class PageParams(
    val size: Int = 10,
    val index: Int = 0
)

object ReqUser {


    suspend fun getUserPage(pageParams: PageParams?): PageVO<UserVO> {
        val response = http.get(ServerRoute.USER) {
            parameter(ServerParams.PAGE_SIZE, pageParams?.size)
            parameter(ServerParams.PAGE_INDEX, pageParams?.index)
        }

//        if (response.status == HttpStatusCode.Unauthorized) {
//            localStorage.removeItem(RouteAuth)
//        }
        return response.body()
    }

    suspend fun deleteUser(ids: List<Long>): Boolean {
        return http.delete(ServerRoute.USER) {
            parameter("ids", ids.joinToString())
        }.body()
    }

    suspend fun addUser(addUser: UserVO) {
        http.post(ServerRoute.USER) {
            setBody(addUser)
        }
    }
}


object ReqAuth {

    suspend fun auth(baseVO: UserVO): MsgVO<String> {
        val response = http.post(ServerRoute.AUTH) {
            setBody(baseVO)
        }.call.response

        val msgVO = response.body<MsgVO<String>>()

        if (msgVO.code == MsgVO.OK) {

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

object ReqRegion {
    suspend fun province(): List<RegionVO> {
        return http.get(ServerRoute.Region.ROUTE + ServerRoute.Region.PROVINCE).body()
    }

    suspend fun city(provinceId: Int): List<RegionVO> {
        return http.get(ServerRoute.Region.ROUTE + ServerRoute.Region.CITY + "/$provinceId").body()
    }

    suspend fun county(cityId: Int): List<RegionVO> {
        return http.get(ServerRoute.Region.ROUTE + ServerRoute.Region.COUNTY + "/$cityId").body()
    }

}
