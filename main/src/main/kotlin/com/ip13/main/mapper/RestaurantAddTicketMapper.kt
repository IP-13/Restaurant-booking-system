package com.ip13.main.mapper

import com.ip13.main.model.dto.RestaurantAddTicketDto
import com.ip13.main.model.entity.RestaurantAddTicket
import java.time.LocalDateTime

object RestaurantAddTicketMapper {
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