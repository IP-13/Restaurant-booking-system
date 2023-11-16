package com.ip13.main.security.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.security.model.dto.LoginDto
import com.ip13.main.security.model.dto.LoginResponseDto
import com.ip13.main.security.model.dto.RegisterDto
import com.ip13.main.security.model.dto.RegisterResponseDto
import com.ip13.main.security.model.entity.User
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatusCode
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val tokenService: TokenService,
    private val userService: UserService,
    private val passwordEncoder: BCryptPasswordEncoder,
) {
    private val log = getLogger(javaClass)

    fun register(registerDto: RegisterDto): RegisterResponseDto {
        if (userService.existsByName(registerDto.username)) {
            throw CommonException(
                "User with username ${registerDto.username} already exists",
                HttpStatusCode.valueOf(400)
            )
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
            throw CommonException("passwords don't match", HttpStatusCode.valueOf(400))
        }

        return LoginResponseDto(
            token = tokenService.createToken(user),
        )
    }
}