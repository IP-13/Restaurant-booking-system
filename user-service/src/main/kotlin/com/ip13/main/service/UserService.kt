package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import com.ip13.main.model.dto.request.RoleAddRequest
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
    @Suppress("kotlin:S6619")
    override fun loadUserByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw UserNotFoundException("No user with name \'$username\'")
    }

    fun loadByUsernameOrNull(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    fun findAll(): List<User> {
        return userRepository.findAll().toList()
    }

    fun existsByName(name: String): Boolean {
        return userRepository.existsByUsername(name)
    }

    fun addRole(request: RoleAddRequest): Boolean {
        val user = loadUserByUsername(request.username)
        if (user.roles.contains(request.role)) {
            return false
        }
        val updatedRoles = user.roles.toMutableList()

        val isAdded = updatedRoles.add(request.role)

        val updatedUser = User(
            username = user.username,
            password = user.password,
            roles = updatedRoles,
        )

        save(updatedUser)
        return isAdded
    }
}