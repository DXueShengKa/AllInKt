package cn.allin.net

import cn.allin.ServerParams
import cn.allin.api.ApiUser
import cn.allin.net.Req.http
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import io.ktor.client.call.*
import io.ktor.client.request.*
import js.objects.jso
import toolpad.core.UserSession


object ReqUser : ApiUser {
    override suspend fun page(pageIndex: Int?, pageSize: Int?): PageVO<UserVO> {
        return http.get(ApiUser.pathPage) {
            parameter(ServerParams.PAGE_INDEX, pageIndex)
            parameter(ServerParams.PAGE_SIZE, pageSize)
        }.body()
    }

    override suspend fun get(): UserVO? {
        return http.get(ApiUser.USER).body()
    }


    suspend fun userSession(): UserSession? {
        val user: UserVO? = get()
        return if (user != null) {
            jso {
                id = user.id.toString()
                name = user.name
                email = user.email
            }
        } else null
    }

    override suspend fun add(userVO: UserVO) {
        http.post(ApiUser.USER) {
            setBody(userVO)
        }
    }

    override suspend fun update(user: UserVO) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(ids: List<Long>): Int {
        return http.delete(ApiUser.USER) {
            parameter("ids", ids.joinToString())
        }.body()
    }

}
