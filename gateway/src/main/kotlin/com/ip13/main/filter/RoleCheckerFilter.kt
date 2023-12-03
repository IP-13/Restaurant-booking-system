package com.ip13.main.filter

import com.ip13.main.exceptionHandling.exception.TokenNotFoundException
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class RoleCheckerFilter(
    private val tokenService: TokenService,
) : AbstractGatewayFilterFactory<RoleCheckerFilter.Config>(Config::class.java) {
    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            if (!config.isOpen) {
                val header = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.first()

                val token = tokenService.getTokenFromHeader(header) ?: throw TokenNotFoundException()

                if (tokenService.isTokenExpired(token)) {
                    throw ExpiredJwtException(null, null, "token expired")
                }

                val roles = tokenService.getRoles(token)

                if (!roles.contains(config.role?.name)) {
                    throw ResponseStatusException(HttpStatus.FORBIDDEN, "Required role ${config.role?.name}")
                }
            }

            chain.filter(exchange)
        }
    }

    class Config(
        val isOpen: Boolean,
        val role: Role?
    )
}