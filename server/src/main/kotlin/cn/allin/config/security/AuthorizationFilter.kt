package cn.allin.config.security

import cn.allin.config.CacheConfig
import org.springframework.cache.CacheManager
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
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

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val filter = chain.filter(exchange)
        val token = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        if (!token.isNullOrEmpty()) {
            val key = JwtUtil.extractLong(token)

            cacheManager.getCache(CacheConfig.AUTH)?.also { auth ->
                val authentication: Authentication? = auth.get(key, Authentication::class.java)
                if (authentication != null)
                    return filter.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
            }

        }

        return filter
    }

}