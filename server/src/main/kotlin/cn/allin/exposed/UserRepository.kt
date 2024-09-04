package cn.allin.exposed

import cn.allin.config.UserRole
import cn.allin.exposed.entity.UserEntity
import cn.allin.exposed.table.UserTable
import cn.allin.vo.UserVO
import kotlinx.datetime.toJavaLocalDate
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
class UserRepository {

    fun getUserAll(): List<UserEntity> {
        return UserTable.selectAll()
            .asSequence()
            .map {
                UserEntity.wrapRow(it)
            }
            .toList()
    }

    fun add(userVO: UserVO) {
        UserEntity.new {
            birthday = userVO.birthday?.toJavaLocalDate()
            name = userVO.name
            password = userVO.password?: error("密码不能为空")
            userVO.role?.also {
                role = UserRole.valueOf(it)
            }
        }
    }

    fun findByUsername(username: String): UserEntity? {
        val row = UserTable.selectAll()
            .where {
                UserTable.name eq username
            }.firstOrNull()

        return UserEntity.wrapRow(row ?: return null)
    }

    fun findIdByUsername(username: String): UInt? {
       val raw = UserTable.select(UserTable.id)
            .where {
                UserTable.name eq username
            }.firstOrNull()

        return raw?.get(UserTable.id)?.value
    }


    fun findById(id: UInt): UserEntity? {
        return UserEntity.findById(id)
    }

    fun update(user: UserEntity){
        UserTable.update() {

        }
    }
}