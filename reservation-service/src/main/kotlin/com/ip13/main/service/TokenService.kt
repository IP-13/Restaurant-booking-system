package com.ip13.main.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class TokenService {
    @Value("\${security.secret}")
    private lateinit var secret: String

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

    fun isTokenExpired(token: String): Boolean {
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