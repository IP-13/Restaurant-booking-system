package com.ip13.main.service

import com.ip13.main.model.dto.request.UpdateUserRequest
import com.ip13.main.model.dto.response.UserResponse
import com.ip13.main.model.entity.User
import com.ip13.main.model.toUserResponse
import com.ip13.main.repository.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: UserRepository,
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return userRepository.findByUsername(username).map { it as UserDetails }
    }

    fun save(user: User): Mono<User> {
        return userRepository.save(user)
    }

    fun updateUser(updateUserRequest: UpdateUserRequest): Mono<UserResponse> {
        return userRepository.updateUser(
            updateUserRequest.id,
            updateUserRequest.numOfGrades,
            updateUserRequest.sumOfGrades,
            updateUserRequest.roles.map { it.name }
        ).map { it.toUserResponse() }
    }

    fun getAllUsers(): Flux<UserResponse> {
        return userRepository.findAll().map { it.toUserResponse() }
    }
}