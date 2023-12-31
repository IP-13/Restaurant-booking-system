package com.ip13.main.model.dto.request

import com.ip13.main.model.validation.Password
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank
    val username: String,
    @Password
    val password: String,
)

data class LoginRequest(
    @field:NotBlank
    val username: String,
    val password: String,
)