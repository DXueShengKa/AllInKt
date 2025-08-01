package cn.allin.data.repository

import cn.allin.ServerParams
import cn.allin.api.ApiUser
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.annotation.Single

@Single([ApiUser::class])
internal class UserRepository(
    private val http: HttpClient
): ApiUser {

     override suspend fun page(pageIndex: Int?, pageSize: Int?): PageVO<UserVO> {
        return http.get(ApiUser.pathPage) {
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
