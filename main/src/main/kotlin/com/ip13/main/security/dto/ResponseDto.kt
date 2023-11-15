package com.ip13.main.security.dto

import jakarta.validation.constraints.NotBlank

data class RegisterResponseDto(
    @NotBlank
    val token: String,
)

data class LoginResponseDto(
    @NotBlank
    val token: String,
)
