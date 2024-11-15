package cn.allin.controller

import cn.allin.ServerRoute
import cn.allin.repository.UserRepository
import cn.allin.vo.UserVO
import org.springframework.web.bind.annotation.*

/**
 * 用户接口
 */
@RestController
@RequestMapping(ServerRoute.USER)
class UserController(
    private val userRepository: UserRepository,
){

    /**
     * 获取用户列表
     */
    @GetMapping
    fun get(): List<UserVO> {
        return userRepository.getUserAll()
    }

    /**
     * 删除用户
     * @param userId 用户id
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") userId: Long): Boolean {
        return userRepository.delete(userId)
    }

    /**
     * 添加用户
     */
    @PostMapping
    fun add(@RequestBody user: UserVO) {
        userRepository.add(user)
    }
}