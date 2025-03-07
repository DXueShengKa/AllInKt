@file:OptIn(ExperimentalJsStatic::class)

package cn.allin.net

import cn.allin.ServerParams
import cn.allin.ServerRoute
import cn.allin.vo.MsgVO
import cn.allin.vo.PageVO
import cn.allin.vo.RegionVO
import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*


private val http = HttpClient(ktorEngineFactory) {
    commonConfig()
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

            WEKV.authorization.set(msgVO.data!!)

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
