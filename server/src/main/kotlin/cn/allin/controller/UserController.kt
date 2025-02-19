package cn.allin.controller

import cn.allin.PostUserValidator
import cn.allin.PutUserValidator
import cn.allin.ServerParams
import cn.allin.ServerRoute
import cn.allin.repository.UserRepository
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 用户接口
 */
@RestController
@RequestMapping(ServerRoute.USER)
class UserController(
    private val userRepository: UserRepository,
) {

    /**
     * 获取用户列表
     */
    @GetMapping
    fun get(
        @RequestParam(ServerParams.PAGE_INDEX) pageIndex: Int?,
        @RequestParam(ServerParams.PAGE_SIZE) pageSize: Int?
    ): PageVO<UserVO> {
        return userRepository.getUsers(pageIndex ?: 0, pageSize ?: 10)
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
     * 删除用户
     * @param ids 用户id集合
     */
    @DeleteMapping
    fun deleteAll(@RequestParam("ids") ids: List<Long>): Boolean {
        return userRepository.delete(ids)
    }

    /**
     * 添加用户
     */
    @PostMapping
    fun add(@Validated(PostUserValidator::class) @RequestBody user: UserVO) {
        userRepository.add(user)
    }


    @PutMapping
    fun update(@Validated(PutUserValidator::class) @RequestBody user: UserVO) {
        userRepository.update(user)
    }
}
