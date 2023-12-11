package com.ip13.main.repository

import com.ip13.main.model.entity.User
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveCrudRepository<User, Int> {
    @Modifying
    @Query(
        "UPDATE user_t SET num_of_grades = num_of_grades + 1, sum_of_grades = sum_of_grades + :grade" +
                " WHERE username = :username"
    )
    fun addGrade(
        @Param("username")
        username: String,
        @Param("grade")
        grade: Int
    ): Mono<Int>

    @Query("SELECT sum_of_grades * 1.0 / num_of_grades FROM user_t WHERE username = :username")
    fun getGrade(
        @Param("username")
        username: String
    ): Mono<Float>

    fun findByUsername(username: String): Mono<User>
}