package com.ip13.main.security.configuration

import com.ip13.main.security.service.TokenService
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtRequestFilter(
    val tokenService: TokenService,
    val userService: UserService
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
                val user = userService.loadUserByUsername(username as String)

                val authentication = UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    user.authorities,
                )

                // username != null, if and only if jwt != null
                if (tokenService.isTokenValid(jwt!!, user)) {
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