package com.ip13.main.model

import com.ip13.main.model.dto.request.AddBookingConstraintRequest
import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.dto.request.TableReserveRequest
import com.ip13.main.model.dto.response.RestaurantResponse
import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.model.enums.RestaurantAddStatus
import com.ip13.main.model.enums.TableReserveStatus
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
    )
}

fun Restaurant.toRestaurantResponse(): RestaurantResponse {
    return RestaurantResponse(
        id = this.id,
        restaurantAddTicketId = this.restaurantAddTicket.id,
        managerId = this.managerId,
        name = this.name,
        country = this.country,
        city = this.city,
        street = this.street,
        building = this.building,
        entrance = this.entrance,
        floor = this.floor,
        description = this.description,
    )
}

fun TableReserveRequest.toTableReserveTicket(
    restaurant: Restaurant,
    userId: Int,
    managerComment: String? = null,
    status: TableReserveStatus = TableReserveStatus.PROCESSING,
): TableReserveTicket {
    return TableReserveTicket(
        restaurant = restaurant,
        userId = userId,
        creationDate = LocalDateTime.now(),
        fromDate = this.fromDate,
        tillDate = this.tillDate,
        numOfGuests = this.numOfGuests,
        userComment = this.userComment,
        managerComment = managerComment,
        status = status,
    )
}

fun AddBookingConstraintRequest.toBookingConstraint(restaurant: Restaurant, managerId: Int): BookingConstraint {
    return BookingConstraint(
        restaurant = restaurant,
        managerId = managerId,
        reason = this.reason,
        fromDate = this.fromDate,
        tillDate = this.tillDate,
    )
}
