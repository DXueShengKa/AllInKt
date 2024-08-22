package cn.allin.data.repository

import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.annotation.Factory

@Factory
class UserRepository(
    private val httpClient: HttpClient
) {

    suspend fun getUser():List<UserVO> {
        return httpClient.get("user").body()
    }
}