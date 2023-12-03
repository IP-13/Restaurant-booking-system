package com.ip13.main.security.repository

import com.ip13.main.security.model.entity.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Int> {
    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean

    @Modifying
    @Query(
        "update user_t set num_of_grades = :num_of_grades, sum_of_grades = :sum_of_grades, " +
                "roles = :roles where user_t.id = :id", nativeQuery = true
    )
    fun updateUser(
        @Param(value = "id")
        userId: Int,
        @Param(value = "num_of_grades")
        numOfGrades: Int,
        @Param(value = "sum_of_grades")
        sumOfGrades: Int,
        @Param(value = "roles")
        roles: List<String>,
    ): User
}