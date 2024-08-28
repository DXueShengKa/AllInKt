package cn.allin.controller

import cn.allin.config.security.SecurityConfig
import cn.allin.service.LoginService
import cn.allin.utils.SpringUser
import cn.allin.utils.id
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class AuthController(
    private val loginService: LoginService,
) {

    @DeleteMapping
    fun delete(): MsgVO {
        val context = SecurityContextHolder.getContext()
        val authentication = context.authentication
        val principal = authentication.principal
        if (principal is SpringUser){
            SecurityConfig.map.remove(principal.id)
        } else if (principal is Int){
            SecurityConfig.map.remove(principal)
        }
        context.authentication = null

        return MsgVO("退出登录")
    }


    @PostMapping
    fun post(@RequestBody userVO: UserVO, response: HttpServletResponse): MsgVO {

        val token = loginService.login(userVO.name, userVO.password ?: return MsgVO("请输入密码"))

        response.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            addHeader(HttpHeaders.AUTHORIZATION, token)
        }

        return MsgVO("登录成功")
    }
}