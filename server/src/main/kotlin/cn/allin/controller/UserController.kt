package cn.allin.controller

import cn.allin.exposed.UserRepository
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

    @GetMapping(
//        produces = [MediaType.APPLICATION_PROTOBUF_VALUE]
    )
    fun get(): List<UserVO> {
        return userRepository.getUserAll().map { it.toVo() }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") userId: Int): Boolean {
        return transaction {
            UserTable.deleteWhere { id eq userId } > 0
        }
    }

    @PostMapping(
//        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun add(@RequestBody user: UserVO) {
        userRepository.add(user)
    }
}