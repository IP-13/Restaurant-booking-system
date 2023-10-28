package com.ip13.main.mapper

import com.ip13.main.model.dto.RestaurantAddTicketDto
import com.ip13.main.model.entity.Address

object AddressMapper {
    fun addressFromRestaurantAddTicketDto(restaurantAddTicketDto: RestaurantAddTicketDto): Address {
        return Address(
            country = restaurantAddTicketDto.country,
            city = restaurantAddTicketDto.city,
            street = restaurantAddTicketDto.street,
            building = restaurantAddTicketDto.building,
            entrance = restaurantAddTicketDto.entrance,
            floor = restaurantAddTicketDto.floor,
        )
    }
}