package cn.allin.config.security

import cn.allin.config.UserRole
import cn.allin.vo.MsgVO
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig {


    private val scSuccessHandler = AuthenticationSuccessHandler { request, response, authentication ->
        response.status = HttpServletResponse.SC_OK
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        val j = buildJsonObject {
            put("msg", "成功")
        }
        response.writer.use {
            it.write(j.toString())
        }
    }

    private val scEntryPoint = AuthenticationEntryPoint { request, response, authException ->

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        val msgVO: MsgVO<String> = when (authException) {
            is BadCredentialsException -> {
                MsgVO(
                    authException.message.toString(),
                    MsgVO.USER_AUTH_ERR
                )
            }

            else -> {
                MsgVO("未登录", MsgVO.USER_AUTH_ERR)
            }
        }

        response.writer.use {
            it.write(Json.encodeToString(msgVO))
        }
    }

    private val adHandler = AccessDeniedHandler { request, response, authException ->
        val msgVO: MsgVO<String> = when (authException) {
            else -> {
                MsgVO(authException.message.toString(), MsgVO.USER_AUTH_ERR)
            }
        }

        response.writer.use {
            it.write(Json.encodeToString(msgVO))
        }
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity, cacheManager: CacheManager): SecurityFilterChain {
        httpSecurity {
            csrf { disable() }

            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.NEVER }

            authorizeHttpRequests {
                authorize("/auth", permitAll)

                authorize(HttpMethod.GET, "/user", hasAuthority(UserRole.ROLE_ADMIN.name))
                authorize(anyRequest, authenticated)
            }

            formLogin {
                loginPage = "/auth"
                authenticationSuccessHandler = scSuccessHandler
                permitAll()
                disable()
            }

            logout {
                logoutUrl = "/auth"
                permitAll()
                disable()
            }

            exceptionHandling {
                accessDeniedHandler = adHandler
                authenticationEntryPoint = scEntryPoint
            }

            addFilterBefore<BasicAuthenticationFilter>(AuthorizationFilter(cacheManager))

        }

        return httpSecurity.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

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