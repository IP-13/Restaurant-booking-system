package com.ip13.main.repository

import com.ip13.main.model.entity.Restaurant
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : CrudRepository<Restaurant, Int> {
    fun findByManagerId(managerId: Int): Restaurant?

    @Modifying
    @Query(
        "update restaurant set num_of_grades = num_of_grades+1, sum_of_grades = sum_of_grades + :grade where id = :id",
        nativeQuery = true
    )
    fun addGrade(
        @Param(value = "id")
        id: Int,
        @Param(value = "grade")
        grade: Int,
    ): Int
}