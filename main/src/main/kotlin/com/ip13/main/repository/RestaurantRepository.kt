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
    fun findByManagerId(managerId: Int): Restaurant
}