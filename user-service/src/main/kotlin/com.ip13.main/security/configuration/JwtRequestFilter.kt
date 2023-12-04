package com.ip13.main.security.configuration

import com.ip13.main.exceptionHandling.exception.TokenNotFoundException
import com.ip13.main.service.TokenService
import com.ip13.main.util.getLogger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtRequestFilter(
    val tokenService: TokenService,
) : WebFilter {
    private val log = getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authentication = try {
            val header = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.first()

            val token = tokenService.getTokenFromHeader(header) ?: throw TokenNotFoundException()

            if (tokenService.isTokenExpired(token)) {
                throw ExpiredJwtException(null, null, "token expired")
            }

            val username = try {
                tokenService.getUsername(token)
            } catch (ex: SignatureException) {
                log.debug("Wrong signature")
            }

            log.debug("username extracted: {}", username)

            // TODO() в auth-service выполняется запрос, но потом приходит ответ 403 FORBIDDEN
//        webClient.get().uri("/auth/user/ip13").headers { it.setBearerAuth(token) }.retrieve()
//            .bodyToMono(User::class.java).log().subscribe(::println)

            val authorities = tokenService.getRoles(token).map { GrantedAuthority { it } }

            val authentication = UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities,
            )

            log.debug("Authentication found\n{}", authentication.toString())

            authentication
        } catch (_: TokenNotFoundException) {
            return chain.filter(exchange)
        }

        return chain.filter(exchange).contextWrite { ReactiveSecurityContextHolder.withAuthentication(authentication) }
    }
}