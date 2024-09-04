package cn.allin.controller

import cn.allin.exposed.UserRepository
import cn.allin.exposed.entity.toVo
import cn.allin.exposed.table.UserTable
import cn.allin.vo.UserVO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userRepository: UserRepository
){

    @GetMapping
    fun get(): List<UserVO> {
        return userRepository.getUserAll().map { it.toVo() }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") userId: UInt): Boolean {
        return transaction {
            UserTable.deleteWhere { id eq userId } > 0
        }
    }

    @PostMapping
    fun add(@RequestBody user: UserVO) {
        userRepository.add(user)
    }
}