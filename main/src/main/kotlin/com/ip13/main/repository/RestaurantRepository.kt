package com.ip13.main.repository

import com.ip13.main.model.entity.Restaurant
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : CrudRepository<Restaurant, Int> {
    @Transactional
    @Modifying
    @Query(
        "update restaurant set num_of_grades = num_of_grades + 1, sum_of_grades = sum_of_grades + :grade " +
                "where restaurant_id = :restaurant_id",
        nativeQuery = true
    )
    fun addGrade(
        @Param("restaurant_id")
        restaurantId: Int,
        @Param("grade")
        grade: Int
    ): Int

    @Query(
        "select cast(sum_of_grades as float)/num_of_grades where restaurant_id = :restaurant_id",
        nativeQuery = true
    )
    fun getGrade(
        @Param("restaurant_id")
        restaurantId: Int,
    ): Float
}