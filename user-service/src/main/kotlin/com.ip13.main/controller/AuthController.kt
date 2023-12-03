package com.ip13.main.controller

import com.ip13.main.model.dto.request.LoginRequest
import com.ip13.main.model.dto.request.RegisterRequest
import com.ip13.main.model.dto.request.UpdateUserRequest
import com.ip13.main.model.dto.response.LoginResponse
import com.ip13.main.model.dto.response.RegisterResponse
import com.ip13.main.model.dto.response.UserResponse
import com.ip13.main.model.entity.User
import com.ip13.main.security.service.AuthService
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration


@Validated
@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
    val userService: UserService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/registration")
    fun register(
        @Valid
        @RequestBody
        request: RegisterRequest,
    ): Mono<RegisterResponse> {
        log.debug("/auth/registration endpoint invoked")

        return authService.register(request = request)
    }

    @PostMapping("/login")
    fun login(
        @Valid
        @RequestBody
        request: LoginRequest
    ): Mono<LoginResponse> {
        log.debug("/auth/login endpoint invoked")

        return authService.login(request = request)
    }

    @GetMapping("/user/{username}")
    fun loadByUsername(
        @PathVariable
        username: String,
    ): Mono<User> {
        return userService.findByUsername(username).map { it as User }
    }

    @GetMapping("user")
    fun getAllUsers(): Flux<*> {
        val users = userService.getAllUsers()
        val interval = Flux.interval(Duration.ofSeconds(2))
        return Flux.zip(users, interval)
    }

    @PutMapping("/user/{id}")
    fun updateUser(
        @RequestBody
        updateUserRequest: UpdateUserRequest,
    ): Mono<UserResponse> {
        return userService.updateUser(updateUserRequest)
    }
}