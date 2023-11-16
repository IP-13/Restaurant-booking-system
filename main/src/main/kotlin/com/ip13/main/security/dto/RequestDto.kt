package com.ip13.main.security.dto

import com.ip13.main.model.validation.Password
import jakarta.validation.constraints.NotBlank

data class RegisterDto(
    @NotBlank
    val username: String,
    @Password
    val password: String,
)

data class LoginDto(
    @NotBlank
    val username: String,
    val password: String,
)