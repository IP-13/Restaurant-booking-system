package com.ip13.main.mapper

import com.ip13.main.model.dto.*
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.Restaurant
import java.time.LocalDateTime

object RestaurantMapper {
    fun restaurantFromRestaurantAddTicket(ticket: RestaurantAddTicket): Restaurant {
        return Restaurant(
            name = ticket.name,
            address = AddressMapper.addressFromRestaurantAddTicket(ticket),
            restaurantAddTicket = ticket,
            description = ticket.description,
        )
    }

    fun restaurantAddTicketDtoToRestaurantAddTicket(dto: RestaurantAddTicketDto): RestaurantAddTicket {
        return RestaurantAddTicket(
            name = dto.name,
            country = dto.country,
            city = dto.city,
            street = dto.street,
            building = dto.building,
            entrance = dto.entrance,
            floor = dto.floor,
            description = dto.description,
            userId = dto.userId,
            createDate = LocalDateTime.now(),
        )
    }
}