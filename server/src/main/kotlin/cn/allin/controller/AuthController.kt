package cn.allin.controller

import cn.allin.config.security.JwtUtil
import cn.allin.service.LoginService
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class AuthController(
    private val loginService: LoginService,
) {

    @DeleteMapping
    fun delete(request: HttpServletRequest): MsgVO {
        val auth = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return MsgVO("未登录")
        val userId = JwtUtil.extractUsername(auth).toInt()
        loginService.logout(userId)

        return MsgVO("退出登录")
    }


    @PostMapping
    fun post(@RequestBody userVO: UserVO, response: HttpServletResponse): MsgVO {
        val userId = loginService.findUserId(userVO.name) ?: return MsgVO("没有这个用户")

        loginService.login(userId, userVO.password ?: return MsgVO("请输入密码"))

        response.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            addHeader(HttpHeaders.AUTHORIZATION, JwtUtil.generateToken(userId.toString()))
        }

        return MsgVO("登录成功")
    }
}