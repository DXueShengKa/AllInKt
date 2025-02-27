package cn.allin.controller

import cn.allin.ServerRoute
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
@RequestMapping(ServerRoute.AUTH)
class AuthController(
    private val loginService: LoginService,
) {

    @DeleteMapping
    fun delete(httpEntity: HttpEntity<Void>): Mono<MsgVO<Unit>> {
        val auth = httpEntity.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull() ?: return Mono.just(MsgVO("未登录"))
        val userId = JwtUtil.extractLong(auth)
        return loginService.logout(userId).map {
            MsgVO("退出登录")
        }
    }


    @PostMapping
    fun post(@RequestBody userVO: UserVO): Mono<MsgVO<String>> {
        val user = userVO.name?.let(loginService::findUserId) ?: return Mono.just(MsgVO("没有这个用户", MsgVO.USER_NOT_FOUND))

        return loginService.login(
            user.id,
            userVO.password ?: return Mono.just(MsgVO("请输入密码", 1)),
            user.role
        ).map {
            MsgVO("登录成功", data = JwtUtil.generateToken(user.id))
        }
    }
}
