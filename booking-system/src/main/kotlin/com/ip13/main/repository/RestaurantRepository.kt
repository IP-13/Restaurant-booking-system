package com.ip13.main.repository

import com.ip13.main.model.entity.Restaurant
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : CrudRepository<Restaurant, Int> {
    fun findByManagerId(managerId: Int): Restaurant?
}