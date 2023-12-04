package com.ip13.main.model

import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.enums.RestaurantAddStatus
import java.time.LocalDateTime

fun RestaurantAddTicketRequest.toRestaurantAddTicket(userId: Int, status: RestaurantAddStatus): RestaurantAddTicket {
    return RestaurantAddTicket(
        name = this.name,
        country = this.country,
        city = this.city,
        street = this.street,
        building = this.building,
        entrance = this.entrance,
        floor = this.floor,
        description = this.description,
        userId = userId,
        creationDate = LocalDateTime.now(),
        status = status,
    )
}

fun RestaurantAddTicket.updateRestaurantAddTicket(
    status: RestaurantAddStatus,
    adminId: Int,
    adminComment: String?,
): RestaurantAddTicket {
    return RestaurantAddTicket(
        id = this.id,
        name = this.name,
        country = this.country,
        city = this.city,
        street = this.street,
        building = this.building,
        entrance = this.entrance,
        floor = this.floor,
        description = this.description,
        userId = this.userId,
        creationDate = this.creationDate,
        status = status,
        adminId = adminId,
        processingDate = LocalDateTime.now(),
        adminComment = adminComment,
    )
}

fun RestaurantAddTicket.toRestaurant(): Restaurant {
    return Restaurant(
        restaurantAddTicket = this,
        // при успешной обработке, пользователь создавший заявку становится менеджером ресторана.
        managerId = this.userId,
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