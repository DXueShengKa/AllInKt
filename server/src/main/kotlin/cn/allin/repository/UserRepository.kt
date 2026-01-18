package cn.allin.repository

import cn.allin.model.UserDTO
import cn.allin.model.toUserDTO
import cn.allin.model.toUserVO
import cn.allin.model.updateBuilder
import cn.allin.table.UserTable
import cn.allin.utils.paginate
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class UserRepository {
    suspend fun getUserAll(): List<UserVO> =
        UserTable
            .select(UserTable.name)
            .map(ResultRow::toUserVO)
            .toList()

    suspend fun getUsers(
        index: Int,
        size: Int,
    ): PageVO<UserVO> =
        UserTable
            .selectAll()
            .paginate(index, size) { it.toUserVO() }

    suspend fun add(userVO: UserVO) {
        UserTable
            .insertAndGetId {
                updateBuilder(it, userVO)
            }.value
    }

    suspend fun findRole(username: String): UserVO? =
        UserTable
            .run {
                select(role)
                    .where(name eq username)
            }.firstOrNull()
            ?.toUserVO()

    suspend fun findPasswordRole(id: Long): UserDTO? =
        UserTable
            .select(UserTable.id, UserTable.role, UserTable.password)
            .where(UserTable.id eq id)
            .limit(1)
            .firstOrNull()
            ?.toUserDTO()

    suspend fun findById(id: Long): UserVO? =
        UserTable
            .selectAll()
            .where { UserTable.id eq id }
            .limit(1)
            .firstOrNull()
            ?.toUserVO()

    suspend fun update(user: UserVO) {
        UserTable.update {
            updateBuilder(it, user)
        }
    }

    suspend fun delete(userId: Long): Boolean =
        UserTable.deleteWhere {
            id eq userId
        } > 0

    suspend fun delete(ids: List<Long>): Int =
        UserTable.deleteWhere {
            id inList ids
        }
}
