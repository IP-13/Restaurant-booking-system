package com.ip13.main.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantAddTicketResultRepository : JpaRepository<RestaurantAddTicketResult, Int>