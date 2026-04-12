package cn.allin.config.security

import cn.allin.config.CacheConfig
import cn.allin.utils.UserAuthenticationToken
import org.springframework.cache.CacheManager
import org.springframework.cache.get
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class AuthorizationFilter(
    private val cacheManager: CacheManager,
//    private val serverSecurityContextRepository: ServerSecurityContextRepository
) : WebFilter {
    //    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
//        if (request is HttpServletRequest) {
//
//            val token: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
//
//            if (!token.isNullOrEmpty()) {
//                val securityContext = SecurityContextHolder.getContext()
//                val key = JwtUtil.extractInt(token)
//
//                cacheManager.getCache(CacheConfig.AUTH)?.also { auth ->
//                    securityContext.authentication = auth.getValue(key)
//                }
//
//            }
//        }
//
//        chain.doFilter(request, response)
//    }

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> {
        val token = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        return chain.filter(exchange).contextWrite { context ->
            if (!token.isNullOrEmpty()) {
                try {
                    // 验证 Token 并提取用户ID
                    if (JwtUtil.validateToken(token)) {
                        val userId = JwtUtil.extractUserId(token)

                        // 从缓存中获取用户认证信息
                        cacheManager.getCache(CacheConfig.AUTH)?.also { auth ->
                            val authentication = auth.get<UserAuthenticationToken>(userId)
                            if (authentication != null) {
                                return@contextWrite ReactiveSecurityContextHolder.withAuthentication(authentication)
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Token 无效或过期，继续执行但不设置认证信息
                }
            }
            context
        }
    }
}
