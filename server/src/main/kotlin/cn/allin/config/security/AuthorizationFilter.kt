package cn.allin.config.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder

class AuthorizationFilter : Filter {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        if (request is HttpServletRequest) {

            val token: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

            if (!token.isNullOrEmpty()) {
                val securityContext = SecurityContextHolder.getContext()
                val key = JwtUtil.extractUsername(token).toInt()
                if (SecurityConfig.map.containsKey(key)) {
                    securityContext.authentication = SecurityConfig.map[key]!!
                }
            }
        }

        chain.doFilter(request, response)
    }

}