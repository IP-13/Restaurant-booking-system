package com.ip13.main.model

import com.ip13.main.model.dto.*
import com.ip13.main.model.entity.*

fun AddressDto.toAddress(): Address {
    return Address(
        country = this.country,
        city = this.city,
        street = this.street,
        building = this.building,
        entrance = this.entrance,
        floor = this.floor,
    )
}

fun RestaurantAddTicket.toRestaurant(): Restaurant {
    return Restaurant(
        name = this.name,
        addressId = this.addressId,
        restaurantAddTicketId = this.id,
        description = this.description,
    )
}

fun TableReserveTicketDto.toTableReserveTicket(userId: Int): TableReserveTicket {
    return TableReserveTicket(
        restaurantId = this.restaurantId,
        userId = userId,
        fromDate = this.fromDate,
        tillDate = this.tillDate,
        numOfGuests = this.numOfGuests,
        userComment = this.userComment,
    )
}

fun BookingConstraintDto.toBookingConstraint(managerId: Int): BookingConstraint {
    return BookingConstraint(
        restaurantId = this.restaurantId,
        managerId = managerId,
        reason = this.reason,
        fromDate = this.fromDate,
        tillDate = this.tillDate,
    )
}

fun GradeVisitorDto.toGradeVisitor(userId: Int, restaurantId: Int): GradeVisitor {
    return GradeVisitor(
        userId = userId,
        tableReserveTicketId = this.tableReserveTicketId,
        restaurantId = restaurantId,
        grade = this.grade,
        comment = this.comment,
    )
}

fun GradeManagerDto.toGradeManager(managerId: Int, userId: Int): GradeManager {
    return GradeManager(
        managerId = managerId,
        tableReserveTicketId = this.tableReserveTicketId,
        userId = userId,
        grade = this.grade,
        comment = this.comment,
    )
}