package cn.allin.repository

import cn.allin.config.UserRole
import cn.allin.model.UserEntity
import cn.allin.model.id
import cn.allin.model.name
import cn.allin.utils.toVO
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
            it.toVO()
        }
    }

    fun getUsers(index: Int, size: Int): PageVO<UserVO> {
        return sqlClient.createQuery(UserEntity::class) {
            select(table)
        }.fetchPage(pageIndex = index, pageSize = size)
            .let { p ->
                PageVO(p.rows.map { it.toVO() }, p.totalRowCount.toInt(), p.totalPageCount.toInt())
            }
    }

    fun add(userVO: UserVO) {
        sqlClient.save(UserEntity {
            birthday = userVO.birthday?.toJavaLocalDate()
            name = userVO.name
            password = userVO.password ?: error("密码不能为空")

            userVO.role?.also {
                role = UserRole.valueOf(it)
            }
            address = userVO.address
            gender = userVO.gender
        }, SaveMode.INSERT_ONLY)
    }

    fun findByUsername(username: String): UserEntity? {
        return sqlClient.executeQuery(UserEntity::class, limit = 1) {
            where(table.name eq username)
            select(table)
        }.firstOrNull()
    }

    fun findIdByUsername(username: String): Long? {
        return sqlClient.executeQuery(UserEntity::class) {
            where(table.name eq username)
            select(table.id)
        }.firstOrNull()
    }


    fun findById(id: Long): UserEntity? {
        return sqlClient.findById(UserEntity::class, id)
    }

    fun update(user: UserVO) {
        sqlClient.createUpdate(UserEntity::class) {
            set(table.name, user.name)
            where(table.id eq user.userId)
        }
    }

    fun delete(userId: Long): Boolean {
        return sqlClient.deleteById(UserEntity::class, userId).totalAffectedRowCount > 0
    }


    fun delete(ids: List<Long>): Boolean {
        return sqlClient.deleteByIds(UserEntity::class,ids).totalAffectedRowCount > 0
    }
}