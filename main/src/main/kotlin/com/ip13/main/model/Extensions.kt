package com.ip13.main.model

import com.ip13.main.model.dto.request.*
import com.ip13.main.model.entity.*
import com.ip13.main.model.enums.RestaurantAddStatus
import com.ip13.main.model.enums.TableReserveStatus
import com.ip13.main.security.model.entity.User
import java.time.LocalDateTime

fun RestaurantAddTicket.toRestaurant(): Restaurant {
    return Restaurant(
        restaurantAddTicket = this,
        // при успешной обработке, пользователь создавший заявку становится менеджером ресторана.
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

fun BlackListRequest.toBlackList(): BlackList {
    return BlackList(
        user = User(this.userId),
        fromDate = this.fromDate,
        tillDate = this.tillDate,
        reason = this.reason,
    )
}

fun TableReserveRequest.toTableReserveTicket(
    restaurant: Restaurant,
    user: User,
    managerComment: String? = null,
    status: TableReserveStatus = TableReserveStatus.PROCESSING,
): TableReserveTicket {
    return TableReserveTicket(
        restaurant = restaurant,
        user = user,
        creationDate = LocalDateTime.now(),
        fromDate = this.fromDate,
        tillDate = this.tillDate,
        numOfGuests = this.numOfGuests,
        userComment = this.userComment,
        managerComment = managerComment,
        status = status,
    )
}

fun RestaurantAddTicketRequest.toRestaurantAddTicket(user: User, status: RestaurantAddStatus): RestaurantAddTicket {
    return RestaurantAddTicket(
        name = this.name,
        country = this.country,
        city = this.city,
        street = this.street,
        building = this.building,
        entrance = this.entrance,
        floor = this.floor,
        description = this.description,
        user = user,
        creationDate = LocalDateTime.now(),
        status = status,
    )
}

fun RestaurantAddTicket.updateRestaurantAddTicket(
    status: RestaurantAddStatus,
    admin: User,
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
        user = this.user,
        creationDate = this.creationDate,
        status = status,
        admin = admin,
        processingDate = LocalDateTime.now(),
        adminComment = adminComment,
    )
}

fun GradeVisitorRequest.toGradeVisitor(
    user: User,
    tableReserveTicket: TableReserveTicket,
    restaurant: Restaurant
): GradeVisitor {
    return GradeVisitor(
        user = user,
        tableReserveTicket = tableReserveTicket,
        restaurant = restaurant,
        grade = this.grade,
        comment = this.comment
    )
}

fun AddBookingConstraintRequest.toBookingConstraint(restaurant: Restaurant, manager: User): BookingConstraint {
    return BookingConstraint(
        restaurant = restaurant,
        manager = manager,
        reason = this.reason,
        fromDate = this.fromDate,
        tillDate = this.tillDate,
    )
}