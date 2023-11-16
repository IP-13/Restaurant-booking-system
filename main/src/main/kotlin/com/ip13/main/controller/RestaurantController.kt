package com.ip13.main.controller

import com.ip13.main.model.dto.request.GradeManagerRequestDto
import com.ip13.main.model.dto.request.GradeVisitorRequestDto
import com.ip13.main.model.dto.request.RestaurantAddTicketRequestDto
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequestDto
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
        restaurantAddTicketRequestDto: RestaurantAddTicketRequestDto,
    ): RestaurantAddTicketResponseDto {
        log.debug("/restaurant/create_ticket endpoint invoked")

        return restaurantAddTicketService.createTicket(authHeader, restaurantAddTicketRequestDto)
    }

    @PostMapping("/process_ticket")
    fun processTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody(required = true)
        dto: RestaurantProcessTicketRequestDto,
    ): RestaurantProcessTicketResponseDto {
        log.debug("/restaurant/process_ticket endpoint invoked")

        return restaurantAddTicketService.processRestaurantAddTicket(authHeader, dto)
    }

    @GetMapping("/show_tickets")
    fun showTickets(
        @RequestHeader(name = "page_number", required = true)
        pageNumber: Int,
        @RequestHeader(name = "page_size", required = true)
        pageSize: Int,
    ): ShowTicketsResponseDto {
        log.debug("/restaurant/show_tickets endpoint invoked")

        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        val tickets = restaurantAddTicketService.getTickets(pageRequest)

        log.debug("tickets found\n{}", tickets.map { it::toString })

        return ShowTicketsResponseDto(tickets)
    }

    @GetMapping("/add_grade_visitor")
    fun addGradeVisitor(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody
        gradeVisitorRequestDto: GradeVisitorRequestDto,
    ): GradeVisitorResponseDto {
        log.debug("/restaurant/add_grade_visitor endpoint invoked")

        val newGrade = gradeVisitorService.gradeRestaurant(authHeader, gradeVisitorRequestDto)

        return GradeVisitorResponseDto(newGrade)
    }

    @GetMapping("/add_grade_manager")
    fun addGradeManager(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @Valid
        @RequestBody
        dto: GradeManagerRequestDto,
    ): GradeManagerResponseDto {
        log.debug("/restaurant/add_grade_manager endpoint invoked")

        return gradeManagerService.gradeUser(authHeader, dto)
    }
}