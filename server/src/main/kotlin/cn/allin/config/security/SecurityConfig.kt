package cn.allin.config.security

import cn.allin.InJson
import cn.allin.config.UserRole
import cn.allin.service.UserService
import cn.allin.vo.MsgVO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import reactor.core.publisher.Mono


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager = true)
class SecurityConfig {


    private val scSuccessHandler = ServerAuthenticationSuccessHandler { exchange, authentication ->
        val response = exchange.exchange.response
        response.statusCode = HttpStatus.OK
        response.headers.contentType = MediaType.APPLICATION_JSON
        response.headers.acceptCharset = listOf(Charsets.UTF_8)

        val string = InJson.encodeToString(buildJsonObject {
            put("msg", "成功")
        })

        val dataBuffer = response.bufferFactory().wrap(string.encodeToByteArray())

        response.writeWith(Mono.just(dataBuffer))
    }

    private val failureHandler = ServerAuthenticationFailureHandler { exchange, authException ->

        val response = exchange.exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        response.headers.contentType = MediaType.APPLICATION_JSON
        response.headers.acceptCharset = listOf(Charsets.UTF_8)

        val msgVO: MsgVO<String> = when (authException) {
            is BadCredentialsException -> {
                MsgVO(
                    authException.message.toString(),
                    MsgVO.USER_AUTH_ERR
                )
            }

            is InsufficientAuthenticationException -> {
                MsgVO(
                    authException.message.toString(),
                    MsgVO.USER_AUTH_ERR
                )
            }

            else -> {
                MsgVO("未登录", MsgVO.USER_AUTH_ERR)
            }
        }

        val buffer = response.bufferFactory().wrap(InJson.encodeToString(msgVO).encodeToByteArray())
        response.writeWith(Mono.just(buffer))
    }

    private val entryPoint = ServerAuthenticationEntryPoint { exchange, authException ->
        val msgVO: MsgVO<String> = when (authException) {
            else -> {
                MsgVO(authException.message.toString(), MsgVO.USER_AUTH_ERR)
            }
        }

        exchange.response.run {

            val buffer = bufferFactory().wrap(Json.encodeToString(msgVO).encodeToByteArray())
            writeWith(Mono.just(buffer))
        }
    }

    @Bean
    fun securityFilterChain(serverHttpSecurity: ServerHttpSecurity, cacheManager: CacheManager): SecurityWebFilterChain {
        return serverHttpSecurity {
            csrf { disable() }

//            sessionManagement {
//                sessionCreationPolicy = SessionCreationPolicy.NEVER
//            }

            authorizeExchange {
                authorize("/auth", permitAll)
//
                authorize("/user/*", hasAuthority(UserRole.ROLE_ADMIN.name))
                authorize(anyExchange, authenticated)
//                authorize(anyExchange, permitAll)
            }

            formLogin {
                loginPage = "/auth"

                authenticationSuccessHandler = scSuccessHandler
//                permitAll()
                authenticationFailureHandler = failureHandler


//                authenticationEntryPoint = entryPoint
                disable()
            }

            logout {
                logoutUrl = "/auth"
//                permitAll()
                disable()
            }

            exceptionHandling {
//                accessDeniedHandler = adHandler
                authenticationEntryPoint = entryPoint
            }

            addFilterBefore(AuthorizationFilter(cacheManager), SecurityWebFiltersOrder.AUTHENTICATION)
        }

    }

    @Bean
    fun authenticationManager(userService: UserService): ReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userService)
    }




    //todo web flux中未生效
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return object : PasswordEncoder {
            override fun encode(rawPassword: CharSequence?): String {
                return rawPassword.toString()
            }

            override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
                return rawPassword != null && rawPassword == encodedPassword
            }

        }
    }


    //    @Bean
//    fun authenticationConverter(): AuthenticationConverter = AuthenticationConverter { request ->
//        val authorization = request.getHeaders(HttpHeaders.AUTHORIZATION)
//        if (!authorization.hasMoreElements())
//            return@AuthenticationConverter SecurityContextHolder.getContext().authentication
//
//        if (JwtUtil.validateToken(authorization.nextElement())) {
//            val username = JwtUtil.extractUsername(authorization.nextElement())
//            return@AuthenticationConverter UsernamePasswordAuthenticationToken.authenticated(
//                username,
//                null,
//                emptyList()
//            )
//        }
//
//        SecurityContextHolder.getContext().authentication
//    }

//    fun jwtD(): JwtDecoder {
//        return NimbusJwtDecoder

//    } //
//    fun jwE() : JwtEncoder {
//        NimbusJwtEncoder()
//    }

}