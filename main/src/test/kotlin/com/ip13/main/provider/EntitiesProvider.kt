package com.ip13.main.provider

import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.RestaurantAddTicketResult
import com.ip13.main.model.entity.enums.RestaurantAddResult
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.Month

object EntitiesProvider {
    fun getDefaultRestaurantAddTicket(
        id: Int = 13,
        name: String = "restaurant",
        country: String = "country",
        city: String = "city",
        street: String = "street",
        building: Int = 13,
        entrance: Int? = 13,
        floor: Int = -2,
        description: String? = "this is a restaurant in the parking lot",
        userId: Int = 13,
        createDate: LocalDateTime = LocalDateTime.of(
            2002,
            Month.APRIL,
            19,
            13,
            13
        )
    ): RestaurantAddTicket {
        return RestaurantAddTicket(
            id = 13,
            name = name,
            country = country,
            city = city,
            street = street,
            building = building,
            entrance = entrance,
            floor = floor,
            description = description,
            userId = userId,
            createDate = createDate
        )
    }

    fun getDefaultRestaurantAddTicketResult(
        id: Int = 13,
        restaurantAddTicketId: Int = 13,
        adminId: Int = 13,
        result: RestaurantAddResult = RestaurantAddResult.ACCEPTED,
        createDate: LocalDateTime = LocalDateTime.now(),
        adminComment: String? = "live long die young",
    ): RestaurantAddTicketResult {
        return RestaurantAddTicketResult(
            id = id,
            restaurantAddTicketId = restaurantAddTicketId,
            adminId = adminId,
            result = result,
            createDate = createDate,
            adminComment = adminComment,
        )
    }
}