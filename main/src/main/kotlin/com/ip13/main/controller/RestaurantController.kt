package com.ip13.main.controller

import com.ip13.main.mapper.RestaurantMapper
import com.ip13.main.model.dto.RestaurantAddTicketDto
import com.ip13.main.repository.AddressRepository
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.repository.RestaurantRepository
import com.ip13.main.service.RestaurantAddTicketService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/restaurant")
class RestaurantController(
    val addressRepository: AddressRepository,
    val restaurantAddTicketService: RestaurantAddTicketService,
    val restaurantRepository: RestaurantRepository,
) {
    @PostMapping("/add_restaurant")
    fun addRestaurant(
        @RequestBody
        restaurantAddTicketDto: RestaurantAddTicketDto,
    ): ResponseEntity<*> {
        // TODO() deduplication, validation
        val restaurantAddTicket = RestaurantMapper.restaurantAddTicketDtoToRestaurantAddTicket(restaurantAddTicketDto)
        restaurantAddTicketService.save(restaurantAddTicket)

        // TODO() answer
        return ResponseEntity("Ticket for adding restaurant successfully created", HttpStatus.OK)
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