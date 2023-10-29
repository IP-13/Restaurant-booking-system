package com.ip13.main.security.configuration

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfiguration(
    val jwtRequestFilter: JwtRequestFilter,
    val authenticationConfiguration: AuthenticationConfiguration,
) {
    // TODO() вынести из хард-кода
    private val secret = "s2UowvHf2hU16VQCMvzESEzh+JCg8NN5OL0gqMpglCggh5OKE+lLmIGLSYqTuacu"

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                // TODO("configure authorize")
//                authorize("/security/register/**", permitAll)
//                authorize("/security/login/**", permitAll)
//                authorize("/admin/get_authentication", permitAll)
//                authorize("/admin/**", hasAuthority(Role.admin.code))
//                authorize("/error/**", permitAll)
//                authorize(anyRequest, authenticated)
                authorize(anyRequest, permitAll)
                authorize("/**", permitAll)
            }
//            oauth2ResourceServer {
//                jwt {
//                    jwtDecoder = jwtDecoder()
//                }
//            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            csrf {
                disable()
            }
//            cors {
//                disable()
//            }
//            headers {
//                frameOptions {
//                    disable()
//                }
//                xssProtection {
//                    disable()
//                }
//            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtRequestFilter)
//            authenticationManager = authenticationManager()
        }

        return http.build()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.getAuthenticationManager()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withSecretKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))).build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}