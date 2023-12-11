package com.ip13.main.repository

import com.ip13.main.model.entity.RestaurantGrade
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantGradeRepository : ReactiveCrudRepository<RestaurantGrade, Int> {
    fun existsByTableReserveTicketId(tableReserveTicketId: Int): Boolean
}