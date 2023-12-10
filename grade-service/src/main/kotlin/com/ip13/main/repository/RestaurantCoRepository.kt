package com.ip13.main.repository

import com.ip13.main.model.entity.Restaurant
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RestaurantCoRepository: CoroutineCrudRepository<Restaurant, Int> {
    @Modifying
    @Query(
        "UPDATE restaurant SET num_of_grades = num_of_grades + 1, sum_of_grades = sum_of_grades + :grade " +
                "WHERE restaurant_id = :restaurant_id"
    )
    suspend fun addGrade(
        @Param("restaurant_id")
        restaurantId: Int,
        @Param("grade")
        grade: Int
    ): Int

    @Query("SELECT sum_of_grades * 1.0 / num_of_grades FROM restaurant WHERE restaurant_id = :restaurant_id")
    suspend fun getGrade(
        @Param("restaurant_id")
        restaurantId: Int
    ): Float

    suspend fun findByRestaurantId(restaurantId: Int): Restaurant?
}