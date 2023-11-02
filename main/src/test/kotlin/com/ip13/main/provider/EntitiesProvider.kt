package com.ip13.main.provider

import com.ip13.main.model.entity.RestaurantAddTicket
import java.time.LocalDateTime
import java.time.Month

object EntitiesProvider {
    fun getDefaultRestaurantAddTicket(): RestaurantAddTicket {
        return RestaurantAddTicket(
            id = 13,
            name = "restaurant",
            country = "country",
            city = "city",
            street = "street",
            building = 13,
            entrance = 13,
            floor = -2,
            description = "this is a restaurant in the parking lot",
            userId = 13,
            createDate = LocalDateTime.of(
                2002,
                Month.APRIL,
                19,
                13,
                13
            )
        )
    }
}