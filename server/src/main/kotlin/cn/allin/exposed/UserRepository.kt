package cn.allin.exposed

import cn.allin.config.UserRole
import cn.allin.exposed.entity.UserEntity
import cn.allin.exposed.table.UserTable
import cn.allin.vo.UserVO
import kotlinx.datetime.toJavaLocalDate
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
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
        UserTable.insert {
            it[birthday] = userVO.birthday?.toJavaLocalDate()
            it[name] = userVO.name
            userVO.password?.also { p ->
                it[password] = p
            }
            userVO.role?.also { r ->
                it[role] = UserRole.valueOf(r)
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

    fun findIdByUsername(username: String): Int? {
       val raw = UserTable.select(UserTable.id)
            .where {
                UserTable.name eq username
            }.firstOrNull()

        return raw?.get(UserTable.id)?.value
    }


    fun findById(id: Int): UserEntity? {
        return UserEntity.findById(id)
    }

    fun update(user: UserEntity){
        UserEntity(id = EntityID(10,UserTable))
        UserTable.update() {

        }
    }
}