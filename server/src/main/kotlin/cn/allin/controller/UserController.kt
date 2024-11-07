package cn.allin.controller

import cn.allin.ServerRoute
import cn.allin.exposed.UserRepository
import cn.allin.vo.UserVO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ServerRoute.USER)
class UserController(
    private val userRepository: UserRepository
){

    @GetMapping
    fun get(): List<UserVO> {
        return userRepository.getUserAll()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") userId: Long): Boolean {
        return userRepository.delete(userId)
    }

    @PostMapping
    fun add(@RequestBody user: UserVO) {
        userRepository.add(user)
    }
}