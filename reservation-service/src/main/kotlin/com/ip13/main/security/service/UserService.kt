package com.ip13.main.security.service

import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import com.ip13.main.security.model.dto.request.UpdateUserRequest
import com.ip13.main.security.model.entity.User
import com.ip13.main.security.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    @Value("\${security.secret}")
    private lateinit var megaAdmin: String

    /**
     * throw UserNotFoundException if user with that name doesn't exist
     */
    override fun loadUserByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw UserNotFoundException("No user with name \'$username\'")
    }

    fun loadByUsernameOrNull(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    fun findByIdOrNull(id: Int): User? {
        return userRepository.findByIdOrNull(id)
    }

    fun findAll(): List<User> {
        return userRepository.findAll().toList()
    }

    fun existsByName(name: String): Boolean {
        return userRepository.existsByUsername(name)
    }

    /**
     * throw UserNotFoundException if user with that id doesn't exist
     */
    fun findByIdOrThrow(id: Int): User {
        return userRepository.findByIdOrNull(id) ?: throw UserNotFoundException("User with id: $id not found")
    }

    fun updateUser(updateUserRequest: UpdateUserRequest): User {
        return userRepository.updateUser(
            updateUserRequest.id,
            updateUserRequest.numOfGrades,
            updateUserRequest.sumOfGrades,
            updateUserRequest.roles.map { it.name }
        )
    }
}