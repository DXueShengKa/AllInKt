package cn.allin.controller

import cn.allin.PostUserValidator
import cn.allin.PutUserValidator
import cn.allin.ServerParams
import cn.allin.api.ApiUser
import cn.allin.apiRoute
import cn.allin.repository.UserRepository
import cn.allin.utils.UserAuthenticationToken
import cn.allin.utils.toUserVO
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder
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
@RequestMapping(ApiUser.USER)
class UserController(
    private val userRepository: UserRepository,
) : ApiUser {

    /**
     * 获取用户列表
     */
    @GetMapping(apiRoute.PAGE)
    override suspend fun page(
        @RequestParam(ServerParams.PAGE_INDEX) pageIndex: Int?,
        @RequestParam(ServerParams.PAGE_SIZE) pageSize: Int?
    ): PageVO<UserVO> {
        return userRepository.getUsers(pageIndex ?: 0, pageSize ?: 10)
    }


    @GetMapping
    override suspend fun get(): UserVO? {
        return ReactiveSecurityContextHolder.getContext()
            .mapNotNull {
                val token = it.authentication as UserAuthenticationToken
                userRepository.findById(token.id)?.toUserVO()
            }
            .awaitSingle()
    }


    @DeleteMapping(apiRoute.PATH_ID)
    override suspend fun delete(@PathVariable id: Long): Boolean {
        return userRepository.delete(userId = id)
    }

    /**
     * 删除用户
     * @param ids 用户id集合
     */
    @DeleteMapping
   override suspend fun deleteAll(@RequestParam("ids") ids: List<Long>): Int {
        return userRepository.delete(ids)
    }

    /**
     * 添加用户
     */
    @PostMapping
    override suspend fun add(@Validated(PostUserValidator::class) @RequestBody user: UserVO) {
        userRepository.add(user)
    }


    @PutMapping
    override suspend fun update(@Validated(PutUserValidator::class) @RequestBody user: UserVO) {
        userRepository.update(user)
    }
}
