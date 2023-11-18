package com.ip13.main.controller

import com.ip13.main.model.dto.request.AddBookingConstraintRequest
import com.ip13.main.model.dto.request.GradeVisitorRequest
import com.ip13.main.model.dto.request.ReservationProcessRequest
import com.ip13.main.model.dto.response.AddBookingConstraintResponse
import com.ip13.main.model.dto.response.GradeVisitorResponse
import com.ip13.main.model.dto.response.ReservationProcessResponse
import com.ip13.main.model.dto.response.ShowReservationsResponse
import com.ip13.main.service.BookingConstraintService
import com.ip13.main.service.GradeManagerService
import com.ip13.main.service.TableReserveService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Validated
@RestController
@RequestMapping("/manager", method = [RequestMethod.POST, RequestMethod.GET])
class ManagerController(
    private val tableReserveService: TableReserveService,
    private val bookingConstraintService: BookingConstraintService,
    private val gradeManagerService: GradeManagerService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/add-booking-constraint")
    fun addBookingConstraint(
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: AddBookingConstraintRequest,
    ): AddBookingConstraintResponse {
        log.debug("/manager/add-booking-constraint endpoint invoked")

        return bookingConstraintService.addBookingConstraint(request, principal.name)
    }

    @GetMapping("/show-reservations")
    fun showRestaurant(
        principal: Principal,
        @PositiveOrZero
        @RequestHeader(name = "page_number", required = true)
        pageNumber: Int,
        @PositiveOrZero
        @RequestHeader(name = "page_size", required = true)
        pageSize: Int,
    ): ShowReservationsResponse {
        log.debug("/manager/show-reservations endpoint invoked")

        return tableReserveService.getReservations(pageNumber, pageSize, principal.name)
    }

    @PostMapping("/process-reservation")
    fun processReservation(
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: ReservationProcessRequest,
    ): ReservationProcessResponse {
        log.debug("/manager/process-reservation endpoint invoked")

        return tableReserveService.processReservation(request, principal.name)
    }

    @PostMapping("/grade-visitor")
    fun gradeVisitor(
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: GradeVisitorRequest,
    ): GradeVisitorResponse {
        log.debug("/manager/grade-visitor endpoint invoked")

        return gradeManagerService.gradeVisitor(request, principal.name)
    }
}