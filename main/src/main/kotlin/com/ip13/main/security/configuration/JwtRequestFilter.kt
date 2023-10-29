package com.ip13.main.security.configuration

import com.ip13.main.security.service.TokenService
import com.ip13.main.security.service.UserService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

@Service
class JwtRequestFilter(
    val tokenService: TokenService,
    val userService: UserService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        val jwt = if (header != null && header.startsWith("Bearer ")) {
            header.substring(7)
        } else {
            null
        }

        val username = if (jwt != null) {
            try {
                tokenService.getUsername(jwt)
            } catch (ex: ExpiredJwtException) {
                // TODO("add logger")
            } catch (ex: SignatureException) {
                // TODO("add logger")
            }
        } else {
            null
        }

        if (jwt != null && username != null && SecurityContextHolder.getContext().authentication == null
            ) {
            val user = userService.loadUserByUsername(username as String)

            val authentication = UsernamePasswordAuthenticationToken(
                username,
                null,
                user.authorities,
            )

            if (tokenService.isTokenValid(jwt, user)) {
                // TODO() logger
                println(authentication)
                SecurityContextHolder.getContext().authentication = authentication

            }
        }

        filterChain.doFilter(request, response)
    }
}