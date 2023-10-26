package com.ip13.main.security.configuration

import com.ip13.main.model.entity.enums.Role
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
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
class SecurityConfiguration(
    @Autowired val jwtRequestFilter: JwtRequestFilter,
) {
    private val secret = "s2UowvHf2hU16VQCMvzESEzh+JCg8NN5OL0gqMpglCggh5OKE+lLmIGLSYqTuacu"

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                // TODO("configure authorize")
                authorize("/admin/**", hasAnyAuthority(Role.ADMIN.code))
                authorize("/manager/**", hasAnyAuthority(Role.MANAGER.code))
                authorize("/visitor/**", hasAnyAuthority(Role.VISITOR.code))
                authorize("/security/register/**", permitAll)
                authorize("/security/login/**", permitAll)
                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer {
                jwt {
                    jwtDecoder = jwtDecoder()
                }
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            csrf {
                disable()
            }
            cors {
                disable()
            }
            headers {
                frameOptions {
                    disable()
                }
                xssProtection {
                    disable()
                }
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtRequestFilter)
        }

        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
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