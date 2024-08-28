package cn.allin.service

import cn.allin.config.security.JwtUtil
import cn.allin.config.security.SecurityConfig
import cn.allin.exposed.UserRepository
import cn.allin.utils.id
import cn.allin.utils.newAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager
) {

    fun login(username: String, password: String): String {

        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)

        val authenticationToken = newAuthenticationToken(user.id.value, user.password)
        val authenticate = authenticationManager.authenticate(authenticationToken)

        SecurityContextHolder.getContext().authentication = authenticate

        val userId = authenticationToken.id
        SecurityConfig.map[userId] = authenticate

        return JwtUtil.generateToken(userId.toString())
    }
}