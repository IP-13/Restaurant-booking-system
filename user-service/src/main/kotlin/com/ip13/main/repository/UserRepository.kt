package com.ip13.main.repository

import com.ip13.main.model.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, String> {
    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean
}