package com.ip13.main.security.service

import com.ip13.main.exceptionHandling.exception.AttemptToOverthrowMegaAdminException
import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import com.ip13.main.model.dto.RoleAddDto
import com.ip13.main.model.dto.RoleDeleteDto
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

    fun findByIdOrThrow(id: Int): User {
        return userRepository.findByIdOrNull(id) ?: throw UserNotFoundException("User with id: $id not found")
    }

    fun addRole(roleAddDto: RoleAddDto): Boolean {
        val user = findByIdOrThrow(roleAddDto.userId)
        if (user.roles.contains(roleAddDto.role)) {
            return false
        }
        val isAdded = user.roles.add(roleAddDto.role)
        save(user)
        return isAdded
    }

    fun deleteRole(roleDeleteDto: RoleDeleteDto): Boolean {
        if (roleDeleteDto.userId == 100) {
            throw AttemptToOverthrowMegaAdminException(
                "Who do you think you are? You cannot delete roles from mage_admin. Next time you'll be banned",
                HttpStatusCode.valueOf(400)
            )
        }

        val user = findByIdOrThrow(roleDeleteDto.userId)
        if (!user.roles.contains(roleDeleteDto.role)) {
            return false
        }
        val isDeleted = user.roles.remove(roleDeleteDto.role)
        save(user)
        return isDeleted
    }

    fun getUserByTokenInHeader(header: String): User {
        return loadUserByUsername(tokenService.getUsername(getTokenFromHeader(header)))
    }

    private fun getTokenFromHeader(header: String): String {
        // header starts with "Bearer ...token here..,"
        return header.substring(7)
    }
}