package com.ip13.main.repository

import com.ip13.main.model.entity.RestaurantGrade
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantGradeCoRepository : CoroutineCrudRepository<RestaurantGrade, Int> {
    suspend fun existsByTableReserveTicketId(tableReserveTicketId: Int): Boolean
}