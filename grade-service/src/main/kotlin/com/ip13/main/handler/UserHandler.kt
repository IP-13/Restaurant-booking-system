package com.ip13.main.handler

import com.ip13.main.model.entity.User
import com.ip13.main.repository.UserRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserHandler(
    private val userRepository: UserRepository,
) {
    fun findByUsername(username: String): Mono<User> {
        return userRepository.findByUsername(username)
    }

    fun addGrade(username: String, grade: Int): Mono<Int> {
        return userRepository.addGrade(username, grade)
    }

    fun save(user: User): Mono<User> {
        return userRepository.save(user)
    }

    fun getGrade(username: String): Mono<Float> {
        return userRepository.getGrade(username)
    }
}