package com.ip13.main.controller

import com.ip13.main.model.dto.request.LoginRequest
import com.ip13.main.model.dto.request.RegisterRequest
import com.ip13.main.model.dto.request.RoleAddRequest
import com.ip13.main.model.dto.response.AddRoleResponse
import com.ip13.main.model.dto.response.LoginResponse
import com.ip13.main.model.dto.response.RegisterResponse
import com.ip13.main.model.dto.response.UserResponse
import com.ip13.main.model.toUserResponse
import com.ip13.main.service.AuthService
import com.ip13.main.service.UserService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
    val userService: UserService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/role")
    fun addRole(
        @Valid
        @RequestBody(required = true)
        request: RoleAddRequest,
    ): String {
        log.debug("/auth/role endpoint invoked")
        log.debug("request: {}", request)

        val isAdded = userService.addRole(request)

        val response = if (isAdded) {
            AddRoleResponse("Role ${request.role} successfully added to user ${request.username}")
        } else {
            AddRoleResponse("User ${request.username} already has role ${request.role}")
        }

        log.debug("response: {}", response)
        return response.response
    }

    @GetMapping("/user/{username}")
    fun getUserByUsername(
        @PathVariable
        username: String,
    ): UserResponse? {
        log.debug("/auth/user endpoint invoked")

        return userService.loadByUsernameOrNull(username)?.toUserResponse()
    }


    @PostMapping("/registration")
    fun register(
        @Valid
        @RequestBody
        request: RegisterRequest,
    ): RegisterResponse {
        log.debug("/auth/registration endpoint invoked")

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