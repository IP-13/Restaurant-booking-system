package com.ip13.main.security.service

import com.ip13.main.security.entity.User
import com.ip13.main.security.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    fun findById(id: Int): User? {
        return userRepository.findByIdOrNull(id)
    }

    fun existsByName(name: String): Boolean {
        return userRepository.existsByUsername(name)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    override fun loadUserByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException("No user with that name")
    }
}