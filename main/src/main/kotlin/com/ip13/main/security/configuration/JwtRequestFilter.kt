package com.ip13.main.security.configuration

import com.ip13.main.security.service.TokenService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

@Service
class JwtRequestFilter(
    @Autowired val tokenService: TokenService,
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

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val authentication = UsernamePasswordAuthenticationToken(
                username,
                null,
                // если userName != null, то и jwt != null TODO()
                tokenService.getRoles(jwt!!).map { SimpleGrantedAuthority(it) }
            )

            if (tokenService.isTokenExpired(jwt)) {
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}