package com.ip13.main.mapper

import com.ip13.main.model.dto.RestaurantAddTicketDto
import com.ip13.main.model.entity.RestaurantAddTicket
import java.time.LocalDateTime

object RestaurantAddTicketMapper {
    fun fromRestaurantAddTicketDto(
        addressId: Int,
        dto: RestaurantAddTicketDto,
    ): RestaurantAddTicket {
        return RestaurantAddTicket(
            name = dto.name,
            addressId = addressId,
            description = dto.description,
            userId = dto.userId,
        )
    }
}