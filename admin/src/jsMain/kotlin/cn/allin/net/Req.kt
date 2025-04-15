@file:OptIn(ExperimentalJsStatic::class)

package cn.allin.net

import cn.allin.ServerParams
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
import io.ktor.client.request.*


object Req {
    val http = HttpClient(ktorEngineFactory) {
        commonConfig()
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


suspend fun Req.auth(baseVO: UserVO): MsgVO<String> {
    val response = http.post(apiRoute.auth.path) {
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

suspend fun Req.deleteQanda(id: Int): MsgVO<String> {
    val response = http.delete(apiRoute.qanda.path(id))
    return response.body()
}


suspend fun Req.getQaTagPage(pageParams: PageParams?): PageVO<QaTagVO> {
    return http.get(apiRoute.qanda.tag.page.path) {
        parameter(ServerParams.PAGE_SIZE, pageParams?.size)
        parameter(ServerParams.PAGE_INDEX, pageParams?.index)
    }.body()
}
