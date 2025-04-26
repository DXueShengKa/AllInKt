package cn.allin.api

import cn.allin.apiRoute
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import kotlin.jvm.JvmStatic

interface ApiUser {

    suspend fun page(pageIndex: Int?, pageSize: Int?): PageVO<UserVO>

    suspend fun get(): UserVO?

    suspend fun add(user: UserVO)

    suspend fun update(user: UserVO)

    suspend fun delete(id: Long): Boolean


    suspend fun deleteAll(ids: List<Long>): Int


    companion object {
        const val USER = "user"

        const val pathPage = "${USER}/${apiRoute.PAGE}"

        @JvmStatic
        fun pathDelete(userId: String) = "${USER}/$userId"
    }
}
