package com.ip13.main.security

import com.ip13.main.model.enums.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    val jwtRequestFilter: JwtRequestFilter,
    val authenticationConfiguration: AuthenticationConfiguration,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/restaurant/create-ticket", authenticated)
                authorize("/restaurant/process-ticket", hasAuthority(Role.ADMIN.name))
                authorize("/restaurant/id/**", authenticated)
                authorize("/reservation/reserve-table", authenticated)
                authorize("/reservation/process-reservation", hasAuthority(Role.MANAGER.name))
                authorize("/reservation/add-booking-constraint", hasAuthority(Role.MANAGER.name))
                authorize("/reservation/table-reserve-ticket/**", authenticated)
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
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}