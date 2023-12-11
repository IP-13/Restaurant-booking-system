package com.ip13.main.repository

import com.ip13.main.model.entity.RestaurantAddTicket
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantAddTicketRepository : CrudRepository<RestaurantAddTicket, Int>,
    ListPagingAndSortingRepository<RestaurantAddTicket, Int>