package com.ip13.main.security.controller

import com.ip13.main.security.model.dto.LoginDto
import com.ip13.main.security.model.dto.LoginResponseDto
import com.ip13.main.security.model.dto.RegisterDto
import com.ip13.main.security.model.dto.RegisterResponseDto
import com.ip13.main.security.service.AuthService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
        registerDto: RegisterDto,
    ): RegisterResponseDto {
        log.debug("/auth/register endpoint invoked")

        return authService.register(registerDto = registerDto)
    }

    @PostMapping("/login")
    fun login(
        @Valid
        @RequestBody
        loginDto: LoginDto
    ): LoginResponseDto {
        log.debug("/auth/login endpoint invoked")

        return authService.login(loginDto = loginDto)
    }
}