package com.ip13.main.controller

import com.ip13.main.model.dto.request.*
import com.ip13.main.model.dto.response.*
import com.ip13.main.service.BookingConstraintService
import com.ip13.main.service.RestaurantGradeService
import com.ip13.main.service.TableReserveService
import com.ip13.main.service.VisitorGradeService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/reservation")
class ReserveController(
    private val tableReserveService: TableReserveService,
    private val restaurantGradeService: RestaurantGradeService,
    private val visitorGradeService: VisitorGradeService,
    private val bookingConstraintService: BookingConstraintService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/reserve-table")
    fun reserveTable(
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: TableReserveRequest
    ): TableReserveResponse {
        log.debug("/reservation/reserve-table endpoint invoked")

        return tableReserveService.reserveTable(request, principal.name)
    }

    @PostMapping("/grade-restaurant")
    fun gradeRestaurant(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: GradeRestaurantRequest,
    ): GradeRestaurantResponse {
        log.debug("/reservation/grade-restaurant endpoint invoked")

        val newGrade = restaurantGradeService.gradeRestaurant(request, principal.name, authHeader)

        return GradeRestaurantResponse(newGrade)
    }

    @PostMapping("/process-reservation")
    fun processReservation(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: ReservationProcessRequest,
    ): ReservationProcessResponse {
        log.debug("/reservation/process-reservation endpoint invoked")

        return tableReserveService.processReservation(request, principal.name, authHeader)
    }

    @PostMapping("/grade-visitor")
    fun gradeVisitor(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: GradeVisitorRequest,
    ): GradeVisitorResponse {
        log.debug("/reservation/grade-visitor endpoint invoked")

        return visitorGradeService.gradeVisitor(request, principal.name, authHeader)
    }

    @PostMapping("/add-booking-constraint")
    fun addBookingConstraint(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: AddBookingConstraintRequest,
    ): AddBookingConstraintResponse {
        log.debug("/reservation/add-booking-constraint endpoint invoked")

        return bookingConstraintService.addBookingConstraint(request, principal.name, authHeader)
    }
}