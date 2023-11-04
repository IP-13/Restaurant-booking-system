package com.ip13.main.security.controller

import com.ip13.main.security.dto.LoginDto
import com.ip13.main.security.dto.LoginResponseDto
import com.ip13.main.security.dto.RegisterDto
import com.ip13.main.security.dto.RegisterResponseDto
import com.ip13.main.security.service.AuthService
import com.ip13.main.security.service.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/security")
class AuthController(
    val authService: AuthService,
    val tokenService: TokenService,
) {
    @PostMapping("/register")
    fun register(@RequestBody registerDto: RegisterDto): ResponseEntity<RegisterResponseDto> {
        return ResponseEntity.ok(
            authService.register(registerDto = registerDto)
        )
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<LoginResponseDto> {
        return ResponseEntity.ok(
            authService.login(loginDto = loginDto)
        )
    }
}