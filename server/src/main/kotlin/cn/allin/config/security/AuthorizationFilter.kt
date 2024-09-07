package cn.allin.config.security

import cn.allin.config.CacheConfig
import cn.allin.utils.getValue
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.cache.CacheManager
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder

class AuthorizationFilter(
    private val cacheManager: CacheManager
) : Filter {


    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        if (request is HttpServletRequest) {

            val token: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

            if (!token.isNullOrEmpty()) {
                val securityContext = SecurityContextHolder.getContext()
                val key = JwtUtil.extractInt(token)

                cacheManager.getCache(CacheConfig.AUTH)?.also { auth ->
                    securityContext.authentication = auth.getValue(key)
                }

            }
        }

        chain.doFilter(request, response)
    }

}