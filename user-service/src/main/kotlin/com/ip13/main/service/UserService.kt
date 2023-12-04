package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import com.ip13.main.model.entity.User
import com.ip13.main.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    /**
     * throw UserNotFoundException if user with that name doesn't exist
     */
    override fun loadUserByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw UserNotFoundException("No user with name \'$username\'")
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
}