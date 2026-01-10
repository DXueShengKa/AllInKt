package cn.allin.config.security

import cn.allin.AllJson
import cn.allin.vo.MsgVO
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager = true)
class SecurityConfig {
    private val successHandler =
        ServerAuthenticationSuccessHandler { exchange, authentication ->
            val response = exchange.exchange.response
            response.statusCode = HttpStatus.OK
            response.headers.contentType = MediaType.APPLICATION_JSON
            response.headers.acceptCharset = listOf(Charsets.UTF_8)

            val string =
                AllJson.encodeToString(
                    buildJsonObject {
                        put("msg", "成功")
                    },
                )

            val dataBuffer = response.bufferFactory().wrap(string.encodeToByteArray())

            response.writeWith(Mono.just(dataBuffer))
        }

    private val failureHandler =
        ServerAuthenticationFailureHandler { exchange, authException ->

            val response = exchange.exchange.response
            response.statusCode = HttpStatus.UNAUTHORIZED
            response.headers.contentType = MediaType.APPLICATION_JSON
            response.headers.acceptCharset = listOf(Charsets.UTF_8)

            val msgVO: MsgVO<String> =
                when (authException) {
                    is BadCredentialsException -> {
                        MsgVO.error(MsgVO.auth, authException.message)
                    }

                    is InsufficientAuthenticationException -> {
                        MsgVO.error(MsgVO.auth, authException.message)
                    }

                    else -> {
                        MsgVO.error(MsgVO.auth, "未登录")
                    }
                }

            val buffer = response.bufferFactory().wrap(AllJson.encodeToString(msgVO).encodeToByteArray())
            response.writeWith(Mono.just(buffer))
        }

    private val adHandler =
        ServerAccessDeniedHandler { exchange, authException ->
            val msgVO: MsgVO<String> = MsgVO.error(MsgVO.auth, "权限不足")

            exchange.response.run {
                headers.contentType = MediaType.APPLICATION_JSON
                val buffer = bufferFactory().wrap(AllJson.encodeToString(msgVO).encodeToByteArray())
                writeWith(Mono.just(buffer))
            }
        }

    private val entryPoint =
        ServerAuthenticationEntryPoint { exchange, authException ->
            val msgVO: MsgVO<String> =
                when (authException) {
                    is AuthenticationCredentialsNotFoundException -> {
                        MsgVO.error(MsgVO.auth, "没有权限")
                    }

                    else -> {
                        MsgVO.error(MsgVO.auth, authException.message)
                    }
                }

            exchange.response.run {
                headers.contentType = MediaType.APPLICATION_JSON
                val buffer = bufferFactory().wrap(AllJson.encodeToString(msgVO).encodeToByteArray())
                writeWith(Mono.just(buffer))
            }
        }

    /**
     * 跨域配置
     */
    private val corsConfigurationSource =
        UrlBasedCorsConfigurationSource().also {
            val config =
                CorsConfiguration().apply {
                    addAllowedMethod("*")
                    addAllowedHeader("*")
                    addAllowedOrigin("*")
                }
            it.registerCorsConfiguration("/**", config)
        }

    @Bean
    fun corsWebFilter(): CorsWebFilter = CorsWebFilter(corsConfigurationSource)

    @Bean
    fun securityFilterChain(
        serverHttpSecurity: ServerHttpSecurity,
        cacheManager: CacheManager,
    ): SecurityWebFilterChain =
        serverHttpSecurity {
            csrf { disable() }
            cors {
                configurationSource = corsConfigurationSource
            }

//            sessionManagement {
//                sessionCreationPolicy = SessionCreationPolicy.NEVER
//            }

            authorizeExchange {
                authorize(anyExchange, permitAll)
//                authorize("/${apiRoute.auth.AUTH}/**", permitAll)
//
//                authorize(
//                    pathMatchers(HttpMethod.GET, "/${ApiUser.USER}"),
//                    hasAnyAuthority(UserRole.ROLE_USER.name, UserRole.ROLE_ADMIN.name),
//                )
//
//                authorize(pathMatchers(HttpMethod.GET, "/region/**"), permitAll)
//
//                authorize("/${apiRoute.offiAccount.path}/**", permitAll)
//
//                authorize("/${ApiFile.FILE}/**", permitAll)
//
//                authorize(anyExchange, hasAuthority(UserRole.ROLE_ADMIN.name))
            }

            formLogin {
                loginPage = "/auth"

                authenticationSuccessHandler = successHandler
                authenticationFailureHandler = failureHandler

                disable()
            }

            logout {
                logoutUrl = "/auth"
                disable()
            }

            exceptionHandling {
                accessDeniedHandler = adHandler
                authenticationEntryPoint = entryPoint
            }

            addFilterBefore(AuthorizationFilter(cacheManager), SecurityWebFiltersOrder.AUTHENTICATION)
        }

//    @Bean
//    fun authenticationManager(userService: UserService): ReactiveAuthenticationManager {
//        val authenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(userService)
// //        authenticationManager.setPasswordEncoder()
//        return authenticationManager
//    }

    // todo web flux中未生效
//    @Bean
//    fun passwordEncoder(): PasswordEncoder {
//        return object : PasswordEncoder {
//            override fun encode(rawPassword: CharSequence?): String {
//                return rawPassword.toString()
//            }
//
//            override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
//                return rawPassword != null && rawPassword == encodedPassword
//            }
//
//        }
//    }

//    fun jwtD(): JwtDecoder {
//        return NimbusJwtDecoder

//    } //
//    fun jwE() : JwtEncoder {
//        NimbusJwtEncoder()
//    }
}
