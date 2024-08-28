package cn.allin.exposed

import cn.allin.exposed.entity.User
import cn.allin.exposed.table.UserTable
import cn.allin.vo.UserVO
import kotlinx.datetime.toJavaLocalDate
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
class UserRepository {

    fun getUserAll(): List<User> {
        return UserTable.selectAll()
            .asSequence()
            .map {
                User.wrapRow(it)
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
        }
    }

    fun findByUsername(username: String): User? {
        val row = UserTable.selectAll()
            .where {
                UserTable.name eq username
            }.firstOrNull()

        return User.wrapRow(row ?: return null)
    }


    fun findById(id: Int): User? {
        return User.findById(id)
    }
}