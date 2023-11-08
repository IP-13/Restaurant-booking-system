package com.ip13.main.security.configuration

import com.ip13.main.model.enums.Role
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
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
@ConditionalOnProperty(
    name = ["security.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
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
                authorize("/auth/register/**", hasAuthority(Role.ADMIN.name))
                authorize("/auth/login/**", permitAll)
                authorize("/admin/get_authentication", permitAll)
                authorize("/admin/**", hasAuthority(Role.ADMIN.name))
                authorize("/restaurant/process_ticket", hasAuthority(Role.ADMIN.name))
                authorize("/restaurant/create_ticket", permitAll)
                authorize("/reserve/**", permitAll)
                authorize(anyRequest, authenticated)
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            csrf {
                disable()
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtRequestFilter)
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
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}