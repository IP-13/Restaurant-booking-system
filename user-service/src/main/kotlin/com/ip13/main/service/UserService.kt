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

    fun addRole(request: RoleAddRequest): Boolean {
        val user = findByIdOrThrow(request.userId)
        if (user.roles.contains(request.role)) {
            return false
        }
        val updatedRoles = user.roles.toMutableList()

        val isAdded = updatedRoles.add(request.role)

        val updatedUser = User(
            id = user.id,
            username = user.username,
            password = user.password,
            numOfGrades = user.numOfGrades,
            sumOfGrades = user.sumOfGrades,
            roles = updatedRoles,
        )

        save(updatedUser)
        return isAdded
    }
}