package com.ip13.main.model

import com.ip13.main.model.dto.request.BlackListDto
import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.security.entity.User

fun RestaurantAddTicket.toRestaurant(): Restaurant {
    return Restaurant(
        restaurantAddTicket = this,
        manager = this.user,
        name = this.name,
        country = this.country,
        city = this.city,
        street = this.street,
        building = this.building,
        entrance = this.entrance,
        floor = this.floor,
        description = this.description,
        numOfGrades = 0,
        sumOfGrades = 0,
    )
}

fun BlackListDto.toBlackList(): BlackList {
    return BlackList(
        user = User(this.userId),
        fromDate = this.fromDate,
        tillDate = this.tillDate,
        reason = this.reason,
    )
}