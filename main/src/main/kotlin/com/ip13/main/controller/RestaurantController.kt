package com.ip13.main.controller

import com.ip13.main.model.dto.request.GradeManagerRequest
import com.ip13.main.model.dto.request.GradeVisitorRequest
import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequest
import com.ip13.main.model.dto.response.*
import com.ip13.main.service.GradeManagerService
import com.ip13.main.service.GradeVisitorService
import com.ip13.main.service.RestaurantAddTicketService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/restaurant", method = [RequestMethod.POST, RequestMethod.GET])
class RestaurantController(
    private val restaurantAddTicketService: RestaurantAddTicketService,
    private val gradeVisitorService: GradeVisitorService,
    private val gradeManagerService: GradeManagerService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/create_ticket")
    fun createTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: RestaurantAddTicketRequest,
    ): RestaurantAddTicketResponse {
        log.debug("/restaurant/create_ticket endpoint invoked")

        return restaurantAddTicketService.createTicket(authHeader, request)
    }

    @PostMapping("/process_ticket")
    fun processTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        request: RestaurantProcessTicketRequest,
    ): RestaurantProcessTicketResponse {
        log.debug("/restaurant/process_ticket endpoint invoked")

        return restaurantAddTicketService.processRestaurantAddTicket(authHeader, request)
    }

    @GetMapping("/show_tickets")
    fun showTickets(
        @RequestHeader(name = "page_number", required = true)
        pageNumber: Int,
        @RequestHeader(name = "page_size", required = true)
        pageSize: Int,
    ): ShowTicketsResponse {
        log.debug("/restaurant/show_tickets endpoint invoked")

        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        val tickets = restaurantAddTicketService.getTickets(pageRequest)

        log.debug("tickets found\n{}", tickets.map { it::toString })

        return ShowTicketsResponse(tickets)
    }

    @GetMapping("/add_grade_visitor")
    fun addGradeVisitor(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody
        request: GradeVisitorRequest,
    ): GradeVisitorResponse {
        log.debug("/restaurant/add_grade_visitor endpoint invoked")

        val newGrade = gradeVisitorService.gradeRestaurant(authHeader, request)

        return GradeVisitorResponse(newGrade)
    }

    @GetMapping("/add_grade_manager")
    fun addGradeManager(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody
        request: GradeManagerRequest,
    ): GradeManagerResponse {
        log.debug("/restaurant/add_grade_manager endpoint invoked")

        return gradeManagerService.gradeUser(authHeader, request)
    }
}