package com.ip13.main.security.service

import com.ip13.main.security.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.stereotype.Service
import java.security.Key
import java.time.Instant
import java.util.*

@Service
class TokenService {
    private val secret = "s2UowvHf2hU16VQCMvzESEzh+JCg8NN5OL0gqMpglCggh5OKE+lLmIGLSYqTuacu"
    private val lifeTimeSeconds = 120L

    fun createToken(user: User): String {
        val roles = user.authorities.map(GrantedAuthority::getAuthority).toList()

        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(lifeTimeSeconds))
            .subject(user.username)
            .claim("userId", user.id)
            .claim("roles", roles)
            .build()
            .claims

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + lifeTimeSeconds * 1000))
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