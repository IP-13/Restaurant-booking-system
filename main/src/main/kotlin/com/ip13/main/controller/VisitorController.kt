package com.ip13.main.controller

import com.ip13.main.model.dto.request.GradeRestaurantRequest
import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.dto.request.TableReserveRequest
import com.ip13.main.model.dto.response.GradeRestaurantResponse
import com.ip13.main.model.dto.response.RestaurantCreateTicketResponse
import com.ip13.main.model.dto.response.TableReserveResponse
import com.ip13.main.service.RestaurantGradeService
import com.ip13.main.service.RestaurantAddTicketService
import com.ip13.main.service.TableReserveService
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Validated
@RestController
@RequestMapping("/visitor", method = [RequestMethod.POST, RequestMethod.GET])
class VisitorController(
    private val restaurantAddTicketService: RestaurantAddTicketService,
    private val restaurantGradeService: RestaurantGradeService,
    private val tableReserveService: TableReserveService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/reserve-table")
    fun reserveTable(
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: TableReserveRequest
    ): TableReserveResponse {
        log.debug("/visitor/reserve-table endpoint invoked")

        return tableReserveService.reserveTable(request, principal.name)
    }

    @PostMapping("/create-ticket")
    fun createTicketToAddRestaurant(
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: RestaurantAddTicketRequest,
    ): RestaurantCreateTicketResponse {
        log.debug("/visitor/create-ticket endpoint invoked")

        return restaurantAddTicketService.createTicket(request, principal.name)
    }

    @PostMapping("/grade-restaurant")
    fun gradeRestaurant(
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: GradeRestaurantRequest,
    ): GradeRestaurantResponse {
        log.debug("/visitor/grade-restaurant endpoint invoked")

        val newGrade = restaurantGradeService.gradeRestaurant(request, principal.name)

        return GradeRestaurantResponse(newGrade)
    }
}