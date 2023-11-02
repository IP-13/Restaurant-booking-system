package com.ip13.main.provider

import com.ip13.main.model.dto.RestaurantAddTicketDto
import com.ip13.main.model.dto.RestaurantAddTicketResultDto
import com.ip13.main.model.entity.enums.RestaurantAddResult

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

    fun getDefaultRestaurantAddTicketResultDto(): RestaurantAddTicketResultDto {
        return RestaurantAddTicketResultDto(
            restaurantAddTicketId = 13,
            adminId = 13,
            result = RestaurantAddResult.ACCEPTED,
            adminComment = "live long die young",
        )
    }
}