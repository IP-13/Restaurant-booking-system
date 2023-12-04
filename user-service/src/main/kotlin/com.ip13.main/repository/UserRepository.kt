package com.ip13.main.security.repository

import com.ip13.main.model.entity.User
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveCrudRepository<User, Int> {
    fun findByUsername(username: String): Mono<User>

    fun existsByUsername(username: String): Mono<Boolean>

    @Modifying
    @Query(
        "update user_t set num_of_grades = :num_of_grades, sum_of_grades = :sum_of_grades," +
                " roles = :roles where user_t.id = :id"
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
    ): Mono<User>
}