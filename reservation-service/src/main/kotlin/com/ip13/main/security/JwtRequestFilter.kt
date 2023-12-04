package com.ip13.main.security

import com.ip13.main.service.TokenService
import com.ip13.main.util.getLogger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter(
    val tokenService: TokenService,
) : OncePerRequestFilter() {
    private val log = getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val header = request.getHeader("Authorization")

            log.debug("header extracted: {}", header)

            val jwt = tokenService.getTokenFromHeader(header)

            val username = if (jwt != null) {
                try {
                    tokenService.getUsername(jwt)
                } catch (ex: ExpiredJwtException) {
                    log.debug("Jwt token has expired")
                } catch (ex: SignatureException) {
                    log.debug("Wrong signature")
                }
            } else {
                null
            }

            log.debug("username extracted: {}", username)

            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                // username != null, if and only if jwt != null
                val authorities = tokenService.getRoles(jwt!!).map { GrantedAuthority { it } }

                val authentication = UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities,
                )

                if (tokenService.isTokenExpired(jwt)) {
                    log.debug("Authentication found\n{}", authentication.toString())
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } catch (_: Exception) {
            log.debug("No \"Authorization\" header found")
        }

        filterChain.doFilter(request, response)
    }
}