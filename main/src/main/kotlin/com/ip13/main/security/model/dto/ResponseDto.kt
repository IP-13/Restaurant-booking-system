package com.ip13.main.security.model.dto

import jakarta.validation.constraints.NotBlank

data class RegisterResponse(
    @NotBlank
    val token: String,
)

data class LoginResponse(
    @NotBlank
    val token: String,
)
