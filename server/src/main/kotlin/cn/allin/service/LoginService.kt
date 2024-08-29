package cn.allin.service

import cn.allin.config.CacheConfig
import cn.allin.exposed.UserRepository
import cn.allin.utils.newAuthenticationToken
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
) {

    fun findUserId(username: String): Int? {
        return userRepository.findIdByUsername(username)
    }


    @CacheEvict(cacheNames = [CacheConfig.AUTH], key = "#userId")
    fun logout(userId: Int) {
        val context = SecurityContextHolder.getContext()
        context.authentication = null
    }


    @Cacheable(cacheNames = [CacheConfig.AUTH], key = "#userId")
    fun login(userId: Int, password: String): Authentication {
        val authenticate = authenticationManager.authenticate(newAuthenticationToken(userId, password))
        SecurityContextHolder.getContext().authentication = authenticate
        return authenticate
    }

}