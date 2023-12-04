package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import com.ip13.main.model.dto.request.LoginRequest
import com.ip13.main.model.dto.response.LoginResponse
import com.ip13.main.model.dto.request.RegisterRequest
import com.ip13.main.model.dto.request.UpdateUserRequest
import com.ip13.main.model.dto.response.RegisterResponse
import com.ip13.main.model.dto.response.UserResponse
import com.ip13.main.model.entity.User
import com.ip13.main.model.toUserResponse
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AuthService(
    private val tokenService: TokenService,
    private val userService: UserService,
    private val passwordEncoder: BCryptPasswordEncoder,
) {
    private val log = getLogger(javaClass)

    fun register(request: RegisterRequest): Mono<RegisterResponse> {
        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
        )

        return userService.save(user).map {
            RegisterResponse(
                tokenService.createToken(
                    it.username,
                    it.roles.map { role -> role.name },
                )
            )
        }
    }

    fun login(request: LoginRequest): Mono<LoginResponse> {
        val user = userService.findByUsername(request.username)

        log.debug("User found\n{}", user.toString())

        return user.map {
            it as User
            if (!passwordEncoder.matches(request.password, it.password)) {
                throw CommonException("passwords don't match", HttpStatusCode.valueOf(400))
            }

            LoginResponse(
                tokenService.createToken(
                    it.username,
                    it.roles.map { role -> role.name },
                )
            )
        }
    }
}