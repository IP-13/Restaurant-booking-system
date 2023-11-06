package com.ip13.main.controller

import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFoundException
import com.ip13.main.model.dto.GradeVisitorDto
import com.ip13.main.model.dto.RestaurantAddTicketDto
import com.ip13.main.model.dto.RestaurantAddTicketResultDto
import com.ip13.main.model.toGradeVisitor
import com.ip13.main.security.service.UserService
import com.ip13.main.service.*
import com.ip13.main.util.getLogger
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/restaurant", method = [RequestMethod.POST, RequestMethod.GET])
class RestaurantController(
    private val restaurantAddTicketService: RestaurantAddTicketService,
    private val gradeVisitorService: GradeVisitorService,
    private val userService: UserService,
    private val tableReserveService: TableReserveService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/create_ticket")
    fun createTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody(required = true)
        restaurantAddTicketDto: RestaurantAddTicketDto,
    ): ResponseEntity<*> {
        val restaurantAddTicketId = restaurantAddTicketService.saveTransactionalWithAddress(
            restaurantAddTicketDto,
            authHeader,
        )

        return ResponseEntity(
            "Ticket for adding restaurant successfully created with id $restaurantAddTicketId",
            HttpStatus.OK
        )
    }

    @PostMapping("/process_ticket")
    fun processTicketToAddRestaurant(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody(required = true)
        dto: RestaurantAddTicketResultDto,
    ): ResponseEntity<*> {
        val restaurantId = restaurantAddTicketService.processRestaurantAddTicket(authHeader, dto)

        return if (restaurantId != null) {
            ResponseEntity("Restaurant successfully added. New restaurant id $restaurantId", HttpStatus.OK)
        } else {
            ResponseEntity("You have rejected ticket with id ${dto.restaurantAddTicketId}", HttpStatus.OK)
        }
    }

    @GetMapping("/show_tickets")
    fun showTickets(
        @RequestHeader(name = "page_number", required = true)
        pageNumber: Int,
        @RequestHeader(name = "page_size", required = true)
        pageSize: Int,
    ): ResponseEntity<*> {
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        val tickets = restaurantAddTicketService.getTickets(pageRequest)

        log.debug("tickets found\n{}", tickets.map { it::toString })

        return ResponseEntity(tickets, HttpStatus.OK)
    }

    @GetMapping("add_grade_visitor")
    fun addGradeVisitor(
        @RequestHeader(name = "Authorization", required = true)
        authHeader: String,
        @RequestBody
        gradleVisitorDto: GradeVisitorDto,
    ): ResponseEntity<String> {
        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("user extracted from token\n{}", user.toString())

        val tableReserveTicket = tableReserveService.findByIdOrNull(gradleVisitorDto.tableReserveTicketId)
            ?: throw TableReserveTicketNotFoundException(
                "No TableReserveTicket with id ${gradleVisitorDto.tableReserveTicketId}"
            )

        val newGrade = gradeVisitorService.gradeRestaurant(gradleVisitorDto.toGradeVisitor(user.id))

        return ResponseEntity("New grade is $newGrade", HttpStatus.OK)
    }
}