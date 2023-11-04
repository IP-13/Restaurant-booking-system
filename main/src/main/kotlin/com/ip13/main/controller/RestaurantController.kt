package com.ip13.main.controller

import com.ip13.main.mapper.AddressMapper
import com.ip13.main.mapper.RestaurantAddTicketMapper
import com.ip13.main.mapper.RestaurantAddTicketResultMapper.fromRestaurantAddTicketResultDto
import com.ip13.main.model.dto.RestaurantAddTicketDto
import com.ip13.main.model.dto.RestaurantAddTicketResultDto
import com.ip13.main.service.AddressService
import com.ip13.main.service.RestaurantAddTicketService
import com.ip13.main.service.RestaurantService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/restaurant", method = [RequestMethod.POST, RequestMethod.GET])
class RestaurantController(
    val addressService: AddressService,
    val restaurantAddTicketService: RestaurantAddTicketService,
    val restaurantService: RestaurantService,
) {
    @PostMapping("/create_ticket")
    fun createTicketToAddRestaurant(
        @RequestBody
        restaurantAddTicketDto: RestaurantAddTicketDto,
    ): ResponseEntity<*> {
        // TODO() deduplication, validation
        val address = AddressMapper.fromAddressDto(restaurantAddTicketDto.addressDto)

        val addressId = addressService.save(address)

        val restaurantAddTicket = RestaurantAddTicketMapper.fromRestaurantAddTicketDto(
            addressId,
            restaurantAddTicketDto
        )

        val restaurantAddTicketId = restaurantAddTicketService.save(restaurantAddTicket)

        return ResponseEntity(
            "Ticket for adding restaurant successfully created with id $restaurantAddTicketId",
            HttpStatus.OK
        )
    }

    @PostMapping("/process_ticket")
    fun processTicketToAddRestaurant(
        @RequestBody
        dto: RestaurantAddTicketResultDto,
    ): ResponseEntity<*> {
        val restaurantAddTicket = restaurantAddTicketService.findByIdOrNull(dto.restaurantAddTicketId)
            ?: return ResponseEntity(
                "No restaurant add ticket with id ${dto.restaurantAddTicketId}",
                HttpStatus.BAD_REQUEST
            )

        val restaurantAddTicketResult = fromRestaurantAddTicketResultDto(dto)

        val restaurantId = restaurantAddTicketService.processRestaurantAddTicket(
            restaurantAddTicketResult,
            restaurantAddTicket
        )

        return if (restaurantId != null) {
            ResponseEntity("Restaurant successfully added. New restaurant id $restaurantId", HttpStatus.OK)
        } else {
            ResponseEntity("You have rejected ticket with id ${dto.restaurantAddTicketId}", HttpStatus.OK)
        }
    }

    @GetMapping("/show_tickets")
    fun showTickets(
        @RequestHeader(name = "page_number")
        pageNumber: Int,
        @RequestHeader(name = "page_size")
        pageSize: Int,
    ): ResponseEntity<*> {
        val pageRequest = PageRequest.of(pageNumber, pageSize, Sort.unsorted())

        val tickets = restaurantAddTicketService.getTickets(pageRequest)

        return ResponseEntity(tickets, HttpStatus.OK)
    }
}