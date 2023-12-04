package com.ip13.main.service

import com.ip13.main.model.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class TokenService {
    @Value("\${security.secret}")
    private lateinit var secret: String

    @Value("\${security.lifeTimeSeconds}")
    private lateinit var lifeTimeSeconds: String

    fun createToken(user: User): String {
        val roles = user.authorities.map(GrantedAuthority::getAuthority).toList()

        val claims = mapOf<String, Any>("roles" to roles)

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + lifeTimeSeconds.toLong() * 1000))
            .signWith(createSignInKey())
            .compact()
    }

    fun getUsername(token: String): String {
        return getAllClaims(token).subject
    }

    @Suppress("UNCHECKED_CAST")
    fun getRoles(token: String): List<String> {
        return getAllClaims(token).get("roles", List::class.java) as List<String>
    }

    fun getTokenFromHeader(header: String?): String? {
        return if (header != null && header.startsWith("Bearer ")) {
            header.substring(7)
        } else {
            null
        }
    }

    fun isTokenValid(token: String, user: User): Boolean {
        val username = getUsername(token)
        return (username == user.username && !isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean {
        return getAllClaims(token).expiration.before(Date())
    }

    private fun getAllClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(createSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun createSignInKey(): Key {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    }
}