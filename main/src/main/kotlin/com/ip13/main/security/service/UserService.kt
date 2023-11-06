package com.ip13.main.security.service

import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import com.ip13.main.security.entity.User
import com.ip13.main.security.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tokenService: TokenService

    override fun loadUserByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException("No user with that name")
    }

    fun findById(id: Int): User? {
        return userRepository.findByIdOrNull(id)
    }

    fun existsByName(name: String): Boolean {
        return userRepository.existsByUsername(name)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    fun addRole(userId: Int, newRole: String): Boolean {
        return userRepository.addRole(userId, newRole) == 1
    }

    fun addGrade(userId: Int, grade: Int): Int {
        return userRepository.addGrade(userId, grade)
    }

    fun getGrade(userId: Int): Float {
        return userRepository.getGrade(userId)
    }

    /**
     * If user with id=userId doesn't exist in db throw exception, and controllerAdvice will return error 404
     * Doesn't return value. TODO(Use as a checker)
     */
    fun checkUser(userId: Int) {
        if (!userRepository.checkUser(userId)) {
            throw UserNotFoundException("User with id: $userId not found")
        }
    }

    fun checkRole(userId: Int, role: String): Boolean {
        return userRepository.checkRole(userId, role)
    }

    fun getExpirationDateFromBlackList(userId: Int): LocalDateTime? {
        return userRepository.getExpirationDateFromBlackList(userId)
    }

    fun getUserByTokenInHeader(header: String): User {
        return loadUserByUsername(tokenService.getUsername(getTokenFromHeader(header)))
    }

    private fun getTokenFromHeader(header: String): String {
        // header starts with "Bearer ...token here..,"
        return header.substring(7)
    }
}