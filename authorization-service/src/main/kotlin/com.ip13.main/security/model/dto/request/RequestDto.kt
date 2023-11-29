package com.ip13.main.security.model.dto.request

import com.ip13.main.security.model.validation.Password
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank
    val username: String,
    @field:Password
    val password: String,
)

data class LoginRequest(
    @field:NotBlank
    val username: String,
    val password: String,
)