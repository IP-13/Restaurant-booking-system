package com.ip13.main.security.dto

data class RegisterDto(
    val username: String,
    val password: String,
)

data class LoginDto(
    val username: String,
    val password: String,
)