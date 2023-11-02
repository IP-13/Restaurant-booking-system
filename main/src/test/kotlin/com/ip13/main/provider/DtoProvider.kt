package com.ip13.main.provider

import com.ip13.main.model.dto.RestaurantAddTicketDto

object DtoProvider {
    fun getDefaultRestaurantAddTicketDto(): RestaurantAddTicketDto {
        return RestaurantAddTicketDto(
            name = "restaurant name",
            country = "country",
            city = "city",
            street = "street",
            building = 13,
            entrance = 13,
            floor = -2,
            description = "this is a restaurant from parking lot",
            userId = 13,
        )
    }
}