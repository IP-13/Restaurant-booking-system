package com.ip13.main.security.service

import com.ip13.main.security.dto.LoginDto
import com.ip13.main.security.dto.LoginResponseDto
import com.ip13.main.security.dto.RegisterDto
import com.ip13.main.security.dto.RegisterResponseDto
import com.ip13.main.security.entity.User
import com.ip13.main.util.getLogger
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val tokenService: TokenService,
    private val userService: UserService,
    private val passwordEncoder: BCryptPasswordEncoder,
) {
    private val log = getLogger(javaClass)

    fun register(registerDto: RegisterDto): RegisterResponseDto {
        if (userService.existsByName(registerDto.username)) {
            throw ResponseStatusException(400, "User with username ${registerDto.username} already exists", null)
        }

        val user = User(
            username = registerDto.username,
            password = passwordEncoder.encode(registerDto.password),
        )

        val savedUser = userService.save(user)

        return RegisterResponseDto(
            token = tokenService.createToken(savedUser),
        )
    }

    fun login(loginDto: LoginDto): LoginResponseDto {
        val user = userService.loadUserByUsername(loginDto.username)

        log.debug("User found\n{}", user.toString())

        val rawPassword = loginDto.password
        val encodedPassword = user.password

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw ResponseStatusException(400, "passwords don't match", null)
        }

        return LoginResponseDto(
            token = tokenService.createToken(user),
        )
    }
}