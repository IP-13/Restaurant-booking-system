package com.ip13.main.controller

import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequest
import com.ip13.main.model.dto.response.RestaurantCreateTicketResponse
import com.ip13.main.model.dto.response.RestaurantProcessTicketResponse
import com.ip13.main.model.dto.response.RestaurantResponse
import com.ip13.main.model.toRestaurantResponse
import com.ip13.main.util.getLogger
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Validated
@RestController
@RequestMapping("/restaurant")
class RestaurantController(
    private val restaurantAddTicketService: RestaurantAddTicketService,
    private val restaurantService: RestaurantService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/create-ticket")
    fun createTicketToAddRestaurant(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: RestaurantAddTicketRequest,
    ): RestaurantCreateTicketResponse {
        log.debug("/restaurant/create-ticket endpoint invoked")

        return restaurantAddTicketService.createTicket(request, principal.name, authHeader)
    }

    @PostMapping("/process-ticket")
    fun processTicketToAddRestaurant(
        @RequestHeader(value = "Authorization")
        authHeader: String,
        principal: Principal,
        @Valid
        @RequestBody(required = true)
        request: RestaurantProcessTicketRequest,
    ): RestaurantProcessTicketResponse {
        log.debug("/restaurant/process-ticket endpoint invoked")

        return restaurantAddTicketService.processRestaurantAddTicket(request, principal.name, authHeader)
    }

    @GetMapping("/id/{id}")
    fun getRestaurantById(
        @PathVariable
        id: Int,
    ): RestaurantResponse? {
        return restaurantService.findByIdOrNull(id)?.toRestaurantResponse()
    }
}