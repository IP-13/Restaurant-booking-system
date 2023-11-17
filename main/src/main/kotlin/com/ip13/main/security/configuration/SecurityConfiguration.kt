package com.ip13.main.security.configuration

import com.ip13.main.model.enums.Role
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
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/auth/register/**", hasAuthority(Role.ADMIN.name))
                authorize("/auth/login/**", permitAll)
                authorize("/admin/get_authentication", permitAll)
                authorize("/admin/**", hasAuthority(Role.ADMIN.name))
                authorize("/restaurant/process_ticket", hasAuthority(Role.ADMIN.name))
                authorize("/restaurant/show_tickets", hasAuthority(Role.ADMIN.name))
                authorize("/restaurant/add_grade_manager", hasAuthority(Role.MANAGER.name))
                authorize("/restaurant/create_ticket", permitAll)
                authorize("/restaurant/add_grade_visitor", permitAll)
                authorize("/reserve/reserve_table", permitAll)
                authorize("/reserve/**", hasAuthority(Role.MANAGER.name))
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
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}