package com.ip13.main.mapper

import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantAddTicket

object RestaurantMapper {
    fun fromRestaurantAddTicket(ticket: RestaurantAddTicket): Restaurant {
        return Restaurant(
            name = ticket.name,
            addressId = ticket.addressId,
            restaurantAddTicketId = ticket.id,
            description = ticket.description,
        )
    }
}