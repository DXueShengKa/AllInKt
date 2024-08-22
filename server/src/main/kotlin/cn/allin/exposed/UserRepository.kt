package cn.allin.exposed

import cn.allin.exposed.entity.User
import cn.allin.exposed.table.UserTable
import cn.allin.vo.UserVO
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

    fun add(userVO: UserVO){
        UserTable.insert {
            it[age] = userVO.age
            it[name] = userVO.name
        }
    }
}