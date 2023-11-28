package com.ip13.main.security.controller

import com.ip13.main.security.model.dto.request.LoginRequest
import com.ip13.main.security.model.dto.response.LoginResponse
import com.ip13.main.security.model.dto.request.RegisterRequest
import com.ip13.main.security.model.dto.response.RegisterResponse
import com.ip13.main.security.service.AuthService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/register")
    fun register(
        @Valid
        @RequestBody
        request: RegisterRequest,
    ): RegisterResponse {
        log.debug("/auth/register endpoint invoked")

        return authService.register(request = request)
    }

    @PostMapping("/login")
    fun login(
        @Valid
        @RequestBody
        request: LoginRequest
    ): LoginResponse {
        log.debug("/auth/login endpoint invoked")

        return authService.login(request = request)
    }
}