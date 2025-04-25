package cn.allin.repository

import cn.allin.config.UserRole
import cn.allin.model.UserEntity
import cn.allin.model.birthday
import cn.allin.model.email
import cn.allin.model.fetchBy
import cn.allin.model.gender
import cn.allin.model.id
import cn.allin.model.name
import cn.allin.model.password
import cn.allin.utils.toPageVO
import cn.allin.utils.toUserVO
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import kotlinx.datetime.toJavaLocalDate
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val sqlClient: KSqlClient,
) {

    fun getUserAll(): List<UserVO> {

        return sqlClient.executeQuery(UserEntity::class) {
            select(table)
        }.map {
            it.toUserVO()
        }
    }

    fun getUsers(index: Int, size: Int): PageVO<UserVO> {
        return sqlClient.createQuery(UserEntity::class) {
            select(table)
        }.fetchPage(pageIndex = index, pageSize = size)
            .toPageVO { it.toUserVO() }
    }

    fun add(userVO: UserVO) {
        sqlClient.saveCommand(UserEntity {
            birthday = userVO.birthday?.toJavaLocalDate()
            name = userVO.name!!
            password = userVO.password!!

            userVO.role?.also {
                role = UserRole.valueOf(it)
            }
            address = userVO.address
            gender = userVO.gender
        }, SaveMode.INSERT_ONLY).execute()
    }

    fun findRole(username: String): UserEntity? {
        return sqlClient.executeQuery(UserEntity::class, limit = 1) {
            where(table.name eq username)
            select(table.fetchBy {
                role()
            })
        }.firstOrNull()
    }


    fun findPasswordRole(id: Long): UserEntity? {
        return sqlClient.executeQuery(UserEntity::class, limit = 1) {
            where(table.id eq id)
            select(table.fetchBy {
                role()
                password()
            })
        }.firstOrNull()
    }


    fun findById(id: Long): UserEntity? {
        return sqlClient.findById(UserEntity::class, id)
    }

    fun update(user: UserVO) {
        sqlClient.createUpdate(UserEntity::class) {
            user.name?.also {
                set(table.name,it)
            }
            set(table.email,user.email)
            user.password?.also {
                set(table.password,it)
            }
            set(table.birthday,user.birthday?.toJavaLocalDate())
            set(table.gender,user.gender)
            where(table.id eq user.id)
        }
    }

    fun delete(userId: Long): Boolean {
        return sqlClient.deleteById(UserEntity::class, userId).totalAffectedRowCount > 0
    }


    fun delete(ids: List<Long>): Int {
        return sqlClient.deleteByIds(UserEntity::class,ids).totalAffectedRowCount
    }
}
