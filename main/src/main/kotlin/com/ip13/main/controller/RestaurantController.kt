package com.ip13.main.controller

import com.ip13.main.model.dto.request.GradeManagerRequest
import com.ip13.main.model.dto.request.GradeVisitorRequest
import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequest
import com.ip13.main.model.dto.response.*
import com.ip13.main.model.toRestaurantAddTicketResponse
import com.ip13.main.service.GradeManagerService
import com.ip13.main.service.GradeVisitorService
import com.ip13.main.service.RestaurantAddTicketService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/restaurant", method = [RequestMethod.POST, RequestMethod.GET])
class RestaurantController(
    private val restaurantAddTicketService: RestaurantAddTicketService,
    private val gradeVisitorService: GradeVisitorService,
    private val gradeManagerService: GradeManagerService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/create-ticket")
    fun createTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: RestaurantAddTicketRequest,
    ): RestaurantCreateTicketResponse {
        log.debug("/restaurant/create-ticket endpoint invoked")

        return restaurantAddTicketService.createTicket(authHeader, request)
    }

    @PostMapping("/process-ticket")
    fun processTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: RestaurantProcessTicketRequest,
    ): RestaurantProcessTicketResponse {
        log.debug("/restaurant/process-ticket endpoint invoked")

        return restaurantAddTicketService.processRestaurantAddTicket(authHeader, request)
    }

    @GetMapping("/show-tickets")
    fun showTickets(
        @PositiveOrZero
        @RequestHeader(name = "page_number", required = true)
        pageNumber: Int,
        @PositiveOrZero
        @RequestHeader(name = "page_size", required = true)
        pageSize: Int,
    ): ShowTicketsResponse {
        log.debug("/restaurant/show-tickets endpoint invoked")

        val tickets = restaurantAddTicketService.getTickets(pageNumber, pageSize)

        return ShowTicketsResponse(tickets.map { it.toRestaurantAddTicketResponse() })
    }

    @GetMapping("/add-grade-visitor")
    fun addGradeVisitor(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: GradeVisitorRequest,
    ): GradeVisitorResponse {
        log.debug("/restaurant/add-grade-visitor endpoint invoked")

        val newGrade = gradeVisitorService.gradeRestaurant(authHeader, request)

        return GradeVisitorResponse(newGrade)
    }

    @GetMapping("/add-grade-manager")
    fun addGradeManager(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: GradeManagerRequest,
    ): GradeManagerResponse {
        log.debug("/restaurant/add_grade-manager endpoint invoked")

        return gradeManagerService.gradeUser(authHeader, request)
    }
}