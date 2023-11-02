package com.ip13.main.controller

import com.ip13.main.mapper.RestaurantAddTicketMapper
import com.ip13.main.mapper.RestaurantAddTicketResultMapper.restaurantAddTicketResultFromRestaurantAddTicketResultDto
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
        val restaurantAddTicket =
            RestaurantAddTicketMapper.restaurantAddTicketDtoToRestaurantAddTicket(restaurantAddTicketDto)
        restaurantAddTicketService.save(restaurantAddTicket)

        // TODO() answer
        return ResponseEntity("Ticket for adding restaurant successfully created", HttpStatus.OK)
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

        val restaurantAddTicketResult = restaurantAddTicketResultFromRestaurantAddTicketResultDto(dto)

        val restaurantId = restaurantAddTicketService.processRestaurantAddTicket(
            restaurantAddTicketResult,
            restaurantAddTicket
        )

        return if (restaurantId != null) {
            ResponseEntity("Restaurant successfully added. New restaurant id - $restaurantId", HttpStatus.OK)
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

//    @GetMapping("/get/{id}")
//    fun getRestaurant(
//        @PathVariable(required = true)
//        id: Int,
//    ): ResponseEntity<*> {
//        val restaurant = repository.findByIdOrNull(id)
//        return if (restaurant != null) {
//            ResponseEntity(restaurant, HttpStatus.OK)
//        } else {
//            ResponseEntity("No restaurant with id: $id", HttpStatus.OK)
//        }
//    }
//
//    @DeleteMapping("/delete/{id}")
//    fun deleteRestaurant(
//        @PathVariable(required = true)
//        id: Int
//    ): ResponseEntity<String> {
//        repository.deleteById(id)
//        return ResponseEntity.ok("Restaurant with id $id has been deleted")
//    }
//
//    @PutMapping("/update/{id}")
//    fun updateRestaurant(
//        @RequestBody updatedRestaurant: RestaurantRequestDto
//    ): ResponseEntity<> {
//        repository.
//    }
//
//    @PostMapping("/add_manager")
//    fun addManager(
//
//    ) {
//
//    }
}