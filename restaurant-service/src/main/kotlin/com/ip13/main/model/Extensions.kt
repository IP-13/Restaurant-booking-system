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

fun RestaurantAddTicketRequest.toRestaurantAddTicket(
    username: String,
    status: RestaurantAddStatus
): RestaurantAddTicket {
    return RestaurantAddTicket(
        name = this.name,
        country = this.country,
        city = this.city,
        street = this.street,
        building = this.building,
        entrance = this.entrance,
        floor = this.floor,
        description = this.description,
        username = username,
        creationDate = LocalDateTime.now(),
        status = status,
    )
}

fun RestaurantAddTicket.updateRestaurantAddTicket(
    status: RestaurantAddStatus,
    adminName: String,
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
        username = this.username,
        creationDate = this.creationDate,
        status = status,
        adminName = adminName,
        processingDate = LocalDateTime.now(),
        adminComment = adminComment,
    )
}

fun RestaurantAddTicket.toRestaurant(): Restaurant {
    return Restaurant(
        restaurantAddTicket = this,
        // при успешной обработке, пользователь создавший заявку становится менеджером ресторана.
        managerName = this.username,
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
        managerName = this.managerName,
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
    username: String,
    managerComment: String? = null,
    status: TableReserveStatus = TableReserveStatus.PROCESSING,
): TableReserveTicket {
    return TableReserveTicket(
        restaurant = restaurant,
        username = username,
        creationDate = LocalDateTime.now(),
        fromDate = this.fromDate,
        tillDate = this.tillDate,
        numOfGuests = this.numOfGuests,
        userComment = this.userComment,
        managerComment = managerComment,
        status = status,
    )
}

fun AddBookingConstraintRequest.toBookingConstraint(restaurant: Restaurant, managerName: String): BookingConstraint {
    return BookingConstraint(
        restaurant = restaurant,
        managerName = managerName,
        reason = this.reason,
        fromDate = this.fromDate,
        tillDate = this.tillDate,
    )
}
