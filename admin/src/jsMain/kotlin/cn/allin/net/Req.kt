@file:OptIn(ExperimentalJsStatic::class)

package cn.allin.net

import cn.allin.ServerParams
import cn.allin.ServerRoute
import cn.allin.ui.PageParams
import cn.allin.vo.MsgVO
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import cn.allin.vo.QandaVO
import cn.allin.vo.RegionVO
import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*


object Req {
    val http = HttpClient(ktorEngineFactory) {
        commonConfig()
    }
}

suspend fun Req.getUserPage(pageParams: PageParams?): PageVO<UserVO> {
    val response = http.get(ServerRoute.USER + '/' + ServerRoute.PAGE) {
        parameter(ServerParams.PAGE_SIZE, pageParams?.size)
        parameter(ServerParams.PAGE_INDEX, pageParams?.index)
    }
    return response.body()
}


suspend fun Req.deleteUser(ids: List<Long>): Boolean {
    return http.delete(ServerRoute.USER) {
        parameter("ids", ids.joinToString())
    }.body()
}

suspend fun Req.addUser(addUser: UserVO) {
    http.post(ServerRoute.USER) {
        setBody(addUser)
    }
}


suspend fun Req.auth(baseVO: UserVO): MsgVO<String> {
    val response = http.post(ServerRoute.AUTH) {
        setBody(baseVO)
    }.call.response

    val msgVO = response.body<MsgVO<String>>()

    if (msgVO.message == MsgVO.success) {

        WEKV.authorization.set(msgVO.data!!)

        response.headers.forEach { s, strings ->
            println("$s $strings")
        }
        console.log(response.headers)
    }

    return msgVO
}


suspend fun Req.regionProvince(): List<RegionVO> {
    return http.get(ServerRoute.Region.ROUTE + ServerRoute.Region.PROVINCE).body()
}

suspend fun Req.regionCity(provinceId: Int): List<RegionVO> {
    return http.get(ServerRoute.Region.ROUTE + ServerRoute.Region.CITY + "/$provinceId").body()
}

suspend fun Req.regionCounty(cityId: Int): List<RegionVO> {
    return http.get(ServerRoute.Region.ROUTE + ServerRoute.Region.COUNTY + "/$cityId").body()
}


suspend fun Req.getQandaPage(pageParams: PageParams?): PageVO<QandaVO> {
    val response = http.get(ServerRoute.Qanda.ROUTE + '/' + ServerRoute.PAGE) {
        parameter(ServerParams.PAGE_SIZE, pageParams?.size)
        parameter(ServerParams.PAGE_INDEX, pageParams?.index)
    }
    return response.body()
}

suspend fun Req.addQanda(vo: QandaVO): MsgVO<Int> {
    val response = http.post(ServerRoute.Qanda.ROUTE) {
        setBody(vo)
    }
    return response.body()
}

suspend fun Req.deleteQanda(id: Int): MsgVO<String> {
    val response = http.delete(ServerRoute.Qanda.ROUTE + "/${id}")
    return response.body()
}


suspend fun Req.getQaTagPage(pageParams: PageParams?): PageVO<QaTagVO> {
    return http.get(ServerRoute.Qanda.ROUTE + "/" + ServerRoute.Qanda.TAG_PAGE) {
        parameter(ServerParams.PAGE_SIZE, pageParams?.size)
        parameter(ServerParams.PAGE_INDEX, pageParams?.index)
    }.body()
}
