package com.ip13.main.security.model.dto.response

data class RegisterResponse(
    val token: String,
)

data class LoginResponse(
    val token: String,
)
