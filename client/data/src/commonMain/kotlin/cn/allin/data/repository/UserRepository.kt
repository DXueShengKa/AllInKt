package cn.allin.data.repository

import cn.allin.ServerParams
import cn.allin.api.ApiUser
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.annotation.Single

@Single([ApiUser::class])
internal class UserRepository(
    private val http: HttpClient
): ApiUser {

     override suspend fun page(pageIndex: Int?, pageSize: Int?): PageVO<UserVO> {
        return http.get(ApiUser.PATH_PAGE) {
            parameter(ServerParams.PAGE_INDEX, pageIndex)
            parameter(ServerParams.PAGE_SIZE, pageSize)
        }.body()
    }

    override suspend fun get(): UserVO? {
        return http.get(ApiUser.USER).body()
    }


    override suspend fun add(user: UserVO) {
        http.post(ApiUser.USER) {
            setBody(user)
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
