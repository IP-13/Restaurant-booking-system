package com.ip13.main.security.model.dto

import com.ip13.main.model.validation.Password
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @NotBlank
    val username: String,
    @Password
    val password: String,
)

data class LoginRequest(
    @NotBlank
    val username: String,
    val password: String,
)