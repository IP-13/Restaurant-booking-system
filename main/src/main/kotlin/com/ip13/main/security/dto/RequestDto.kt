package com.ip13.main.security.dto

data class RegisterDto(
    val name: String,
    val password: String,
)

data class LoginDto(
    val name: String,
    val password: String,
)