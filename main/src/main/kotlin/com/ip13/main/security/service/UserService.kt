package com.ip13.main.security.service

import com.ip13.main.exceptionHandling.exception.AttemptToOverthrowMegaAdminException
import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import com.ip13.main.model.dto.request.RoleAddRequestDto
import com.ip13.main.model.dto.request.RoleDeleteRequestDto
import com.ip13.main.security.entity.User
import com.ip13.main.security.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatusCode
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
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

    fun addRole(roleAddRequestDto: RoleAddRequestDto): Boolean {
        val user = findByIdOrThrow(roleAddRequestDto.userId)
        if (user.roles.contains(roleAddRequestDto.role)) {
            return false
        }
        val isAdded = user.roles.add(roleAddRequestDto.role)
        save(user)
        return isAdded
    }

    fun deleteRole(roleDeleteRequestDto: RoleDeleteRequestDto): Boolean {
        if (roleDeleteRequestDto.userId == 100) {
            throw AttemptToOverthrowMegaAdminException(
                "Who do you think you are? You cannot delete roles from mage_admin. Next time you'll be banned",
                HttpStatusCode.valueOf(400)
            )
        }

        val user = findByIdOrThrow(roleDeleteRequestDto.userId)
        if (!user.roles.contains(roleDeleteRequestDto.role)) {
            return false
        }
        val isDeleted = user.roles.remove(roleDeleteRequestDto.role)
        save(user)
        return isDeleted
    }

    fun deleteUser(userId: Int) {
        if (userId == 100) {
            throw AttemptToOverthrowMegaAdminException(
                "Who do you think you are? You cannot delete mage_admin. Next time you'll be banned",
                HttpStatusCode.valueOf(400)
            )
        }
        userRepository.deleteById(userId)
    }

    /**
     * throw UserNotFoundException if you couldn't extract user from header
     */
    fun getUserByTokenInHeader(header: String): User {
        return loadUserByUsername(tokenService.getUsername(getTokenFromHeader(header)))
    }

    private fun getTokenFromHeader(header: String): String {
        // header starts with "Bearer ...token here..,"
        return header.substring(7)
    }
}