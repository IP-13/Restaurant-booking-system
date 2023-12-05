package com.ip13.main.model

import com.ip13.main.model.dto.request.AddBookingConstraintRequest
import com.ip13.main.model.dto.request.GradeRestaurantRequest
import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.model.entity.RestaurantGrade
import com.ip13.main.model.entity.TableReserveTicket

fun AddBookingConstraintRequest.toBookingConstraint(managerId: Int): BookingConstraint {
    return BookingConstraint(
        restaurantId = this.restaurantId,
        managerId = managerId,
        reason = this.reason,
        fromDate = this.fromDate,
        tillDate = this.tillDate,
    )
}

fun GradeRestaurantRequest.toRestaurantGrade(
    userId: Int,
    tableReserveTicket: TableReserveTicket,
    restaurantId: Int,
): RestaurantGrade {
    return RestaurantGrade(
        userId = userId,
        tableReserveTicket = tableReserveTicket,
        restaurantId = restaurantId,
        grade = this.grade,
        comment = this.comment
    )
}