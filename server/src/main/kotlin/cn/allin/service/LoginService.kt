package cn.allin.service

import cn.allin.config.CacheConfig
import cn.allin.config.UserRole
import cn.allin.model.UserEntity
import cn.allin.repository.UserRepository
import cn.allin.utils.UserAuthenticationToken
import cn.allin.utils.newAuthenticationToken
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val authenticationManager: ReactiveAuthenticationManager,
) {

    fun findUserId(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }


    @CacheEvict(cacheNames = [CacheConfig.AUTH], key = "#userId")
    fun logout(userId: Long): Mono<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext()
            .contextWrite {
                it.delete(userId)
            }
    }


    @Cacheable(cacheNames = [CacheConfig.AUTH], key = "#userId")
    fun login(userId: Long, password: String, userRole: UserRole): Mono<Authentication> {
        val token: UserAuthenticationToken = newAuthenticationToken(userId, password, listOf(userRole))

        return authenticationManager.authenticate(token)
            .contextWrite {
                ReactiveSecurityContextHolder.withAuthentication(token)
            }
            .map { token }
    }

}
