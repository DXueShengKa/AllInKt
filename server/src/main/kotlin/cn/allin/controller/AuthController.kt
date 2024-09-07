package cn.allin.controller

import cn.allin.config.security.JwtUtil
import cn.allin.service.LoginService
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class AuthController(
    private val loginService: LoginService,
) {

    @DeleteMapping
    fun delete(request: HttpServletRequest): MsgVO<Unit> {
        val auth = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return MsgVO("未登录")
        val userId = JwtUtil.extractInt(auth)
        loginService.logout(userId)

        return MsgVO("退出登录")
    }


    @PostMapping
    fun post(@RequestBody userVO: UserVO): MsgVO<String> {
        val user = loginService.findUserId(userVO.name) ?: return MsgVO("没有这个用户", MsgVO.USER_NOT_FOUND)

        loginService.login(user.id.value, userVO.password ?: return MsgVO("请输入密码", 1),user.role)

        return MsgVO("登录成功", data = JwtUtil.generateToken(user.id.value.toInt()))
    }
}