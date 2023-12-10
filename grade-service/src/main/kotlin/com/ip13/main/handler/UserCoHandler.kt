package com.ip13.main.handler

import com.ip13.main.model.entity.User
import com.ip13.main.repository.UserCoRepository

class UserCoHandler(
    private val userCoRepository: UserCoRepository,
) {
    suspend fun findByUsername(username: String): User {
        return userCoRepository.findByUsername(username)
    }

    suspend fun addGrade(username: String, grade: Int): Int {
        return userCoRepository.addGrade(username, grade)
    }

    suspend fun save(user: User): User {
        return userCoRepository.save(user)
    }

    suspend fun getGrade(username: String): Float {
        return userCoRepository.getGrade(username)
    }
}