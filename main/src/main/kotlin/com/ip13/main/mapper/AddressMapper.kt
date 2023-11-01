package com.ip13.main.mapper

import com.ip13.main.model.entity.Address
import com.ip13.main.model.entity.RestaurantAddTicket

object AddressMapper {
    fun addressFromRestaurantAddTicket(ticket: RestaurantAddTicket): Address {
        return Address(
            country = ticket.country,
            city = ticket.city,
            street = ticket.street,
            building = ticket.building,
            entrance = ticket.entrance,
            floor = ticket.floor,
        )
    }
}