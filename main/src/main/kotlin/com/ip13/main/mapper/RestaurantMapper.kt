package com.ip13.main.mapper

import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantAddTicket

object RestaurantMapper {
    fun restaurantFromRestaurantAddTicket(ticket: RestaurantAddTicket): Restaurant {
        return Restaurant(
            name = ticket.name,
            address = AddressMapper.addressFromRestaurantAddTicket(ticket),
            restaurantAddTicket = ticket,
            description = ticket.description,
        )
    }
}