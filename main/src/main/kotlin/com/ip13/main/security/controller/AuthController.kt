package com.ip13.main.security.controller

import com.ip13.main.security.dto.LoginDto
import com.ip13.main.security.dto.LoginResponseDto
import com.ip13.main.security.dto.RegisterDto
import com.ip13.main.security.dto.RegisterResponseDto
import com.ip13.main.security.service.AuthService
import com.ip13.main.util.getLogger
import org.springframework.http.ResponseEntity
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
        @RequestBody
        registerDto: RegisterDto,
    ): ResponseEntity<RegisterResponseDto> {
        log.debug("/security/register endpoint invoked")

        return ResponseEntity.ok(
            authService.register(registerDto = registerDto)
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody
        loginDto: LoginDto
    ): ResponseEntity<LoginResponseDto> {
        log.debug("/security/login endpoint invoked")

        return ResponseEntity.ok(
            authService.login(loginDto = loginDto)
        )
    }
}