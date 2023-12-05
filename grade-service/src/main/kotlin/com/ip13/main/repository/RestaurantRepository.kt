package com.ip13.main.repository

import com.ip13.main.model.entity.Restaurant
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : ReactiveCrudRepository<Restaurant, Int> {
    @Modifying
    @Query(
        "UPDATE restaurant SET num_of_grades = num_of_grades + 1, sum_of_grades = sum_of_grades + :grade WHERE id = :id"
    )
    fun addGrade(
        @Param("id")
        id: Int,
        @Param("grade")
        grade: Int
    ): Int
}