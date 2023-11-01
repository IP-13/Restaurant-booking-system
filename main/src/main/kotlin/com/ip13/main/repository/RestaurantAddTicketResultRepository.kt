package com.ip13.main.repository

import com.ip13.main.model.entity.RestaurantAddTicketResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantAddTicketResultRepository : JpaRepository<RestaurantAddTicketResult, Int>