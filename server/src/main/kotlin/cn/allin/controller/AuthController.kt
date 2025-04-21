package cn.allin.controller

import cn.allin.apiRoute
import cn.allin.config.security.JwtUtil
import cn.allin.service.LoginService
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping(apiRoute.auth.AUTH)
class AuthController(
    private val loginService: LoginService,
) {

    @DeleteMapping
    fun delete(httpEntity: HttpEntity<Void>): Mono<MsgVO<String?>> {
        val auth = httpEntity.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull() ?: return Mono.just(MsgVO.success(null))
        val userId = JwtUtil.extractLong(auth)
        return loginService.logout(userId)
            .thenReturn(MsgVO.success("退出登录"))
    }


    @PostMapping
    fun post(@RequestBody userVO: UserVO): Mono<MsgVO<String>> {
        val user = userVO.name?.let(loginService::findLoginUser) ?: return Mono.just(MsgVO.fail(MsgVO.login,"没有这个用户"))

        return loginService.login(
            user.id,
            userVO.password ?: return Mono.just(MsgVO.fail(MsgVO.login,"请输入密码")),
            user.role
        ).map {
            MsgVO.success(JwtUtil.generateToken(user.id))
        }
    }
}
