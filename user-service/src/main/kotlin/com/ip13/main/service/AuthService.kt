package com.ip13.main.service

import com.ip13.main.event.RegistrationEvent
import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.LoginRequest
import com.ip13.main.model.dto.response.LoginResponse
import com.ip13.main.model.dto.request.RegisterRequest
import com.ip13.main.model.dto.response.RegisterResponse
import com.ip13.main.model.entity.User
import com.ip13.main.security.TokenService
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatusCode
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val tokenService: TokenService,
    private val userService: UserService,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val kafkaTemplate: KafkaTemplate<String, RegistrationEvent>,
) {
    private val log = getLogger(javaClass)

    fun register(request: RegisterRequest): RegisterResponse {
        if (userService.existsByName(request.username)) {
            throw CommonException(
                "User with username ${request.username} already exists",
                HttpStatusCode.valueOf(400)
            )
        }

        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
        )

        val savedUser = userService.save(user)

        kafkaTemplate.send(TOPIC, RegistrationEvent(user.username))

        return RegisterResponse(
            token = tokenService.createToken(savedUser),
        )
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userService.loadUserByUsername(request.username)

        log.debug("User found\n{}", user.toString())

        val rawPassword = request.password
        val encodedPassword = user.password

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw CommonException("passwords don't match", HttpStatusCode.valueOf(400))
        }

        return LoginResponse(
            token = tokenService.createToken(user),
        )
    }

    companion object {
        private const val TOPIC = "registration"
    }
}