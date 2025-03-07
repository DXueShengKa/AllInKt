package cn.allin.config.security

import cn.allin.config.CacheConfig
import cn.allin.utils.UserAuthenticationToken
import org.springframework.cache.CacheManager
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

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val filter = chain.filter(exchange)
        val token = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        return filter.contextWrite {
//            val c = ReactiveSecurityContextHolder.getContext()
            if (!token.isNullOrEmpty()) {
                val key = JwtUtil.extractLong(token)
                cacheManager.getCache(CacheConfig.AUTH)?.also { auth ->
                    val authentication = auth.get(key, UserAuthenticationToken::class.java)
                    if (authentication != null)
                        return@contextWrite ReactiveSecurityContextHolder.withAuthentication(
                            authentication
                        )
                }
            }
            it
        }

    }

}
