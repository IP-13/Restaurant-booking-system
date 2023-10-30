package com.ip13.main.security.repository

import com.ip13.main.security.entity.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Int> {
    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean

    // TODO() вынести функции в отдельный репозиторий, не в пакете security
    @Modifying
    @Transactional
    @Query("update user_t set roles = array_append(roles, :role) where user_t.id = :id", nativeQuery = true)
    fun addRole(
        @Param(value = "id")
        userId: Int,
        @Param(value = "role")
        newRole: String
    ): Int

    @Query("select (select count(*) from user_t where user_t.id = :id and :role = ANY(roles)) > 0", nativeQuery = true)
    fun checkRole(
        @Param(value = "id")
        userId: Int,
        @Param(value = "role")
        role: String
    ): Boolean

    @Query("select (select count(*) from user_t where id = :id) > 0", nativeQuery = true)
    fun checkUser(
        @Param(value = "id")
        userId: Int,
    ): Boolean
}