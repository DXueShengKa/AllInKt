package cn.allin.repository

import cn.allin.model.UserTable
import cn.allin.vo.UserVO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    suspend fun getUserAll(): List<UserVO> {
        return suspendTransaction {
            UserTable
                .selectAll()
                .map {
                    UserVO(
                        id = it[UserTable.id].value,
                        name = it[UserTable.name],
                        password = it[UserTable.password],
                    )
                }.toList()
        }
    }

//    fun getUsers(index: Int, size: Int): PageVO<UserVO> {
//        return sqlClient.createQuery(UserEntity::class) {
//            select(table)
//        }.fetchPage(pageIndex = index, pageSize = size)
//            .toPageVO { it.toUserVO() }
//    }
//
//    fun add(userVO: UserVO) {
//        sqlClient.saveCommand(userVO.toEntity(), SaveMode.INSERT_ONLY).execute()
//    }
//
//    fun findRole(username: String): UserEntity? {
//        return sqlClient.executeQuery(UserEntity::class, limit = 1) {
//            where(table.name eq username)
//            select(table.fetchBy {
//                role()
//            })
//        }.firstOrNull()
//    }
//
//
//    fun findPasswordRole(id: Long): UserEntity? {
//        return sqlClient.executeQuery(UserEntity::class, limit = 1) {
//            where(table.id eq id)
//            select(table.fetchBy {
//                role()
//                password()
//            })
//        }.firstOrNull()
//    }

//    fun findById(id: Long): UserEntity? {
//        return sqlClient.findOneById(UserEntity::class, id)
//    }

    fun update(user: UserVO) {
    }

    fun delete(userId: Long): Boolean {
        TODO()
    }

    fun delete(ids: List<Long>): Int {
        TODO()
    }
}
