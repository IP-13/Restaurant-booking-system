package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.RestaurantNotFoundException
import com.ip13.main.feign.restaurantClient.RestaurantClient
import com.ip13.main.feign.restaurantClient.dto.Restaurant
import com.ip13.main.feign.userClient.UserClient
import com.ip13.main.model.dto.request.GradeRestaurantRequest
import com.ip13.main.model.entity.RestaurantGrade
import com.ip13.main.model.toRestaurantGrade
import com.ip13.main.repository.RestaurantGradeRepository
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class RestaurantGradeService(
    private val restaurantGradeRepository: RestaurantGradeRepository,
    private val restaurantClient: RestaurantClient,
    private val userClient: UserClient,
    private val tableReserveService: TableReserveService,
) {

    private val log = getLogger(javaClass)

    fun save(restaurantGrade: RestaurantGrade): Int {
        return restaurantGradeRepository.save(restaurantGrade).id
    }

    fun gradeRestaurant(
        request: GradeRestaurantRequest,
        username: String,
        authHeader: String,
    ): Float {
        val user = userClient.getUserByUsername(authHeader = authHeader, username = username)

        log.debug("user extracted from token\n{}", user.toString())

        val tableReserveTicket = tableReserveService.findByIdOrThrow(request.tableReserveTicketId)

        log.debug("tableReserveTicket loaded from db\n{}", tableReserveTicket.toString())

        if (tableReserveTicket.userId != user.id) {
            throw CommonException(
                "You're trying to grade table reserve ticket which doesn't belong to you",
                HttpStatus.BAD_REQUEST
            )
        }

        if (tableReserveTicket.restaurantGrade != null) {
            throw CommonException(
                "You already left grade to table reserve ticket with id ${tableReserveTicket.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        val restaurant = restaurantClient.getRestaurantById(authHeader, tableReserveTicket.restaurantId)
            ?: throw RestaurantNotFoundException()

        log.debug("Restaurant received from restaurant-service \n{}", restaurant.toString())

        val restaurantGrade = request.toRestaurantGrade(user.id, tableReserveTicket, restaurant.id)

        val updatedRestaurant = addGrade(restaurant, restaurantGrade)

        log.debug("Updated restaurant\n{}", updatedRestaurant)

        // transaction
        restaurantClient.addGrade(authHeader, restaurant.id, request.grade)
        save(restaurantGrade)

        return updatedRestaurant.sumOfGrades.toFloat() / updatedRestaurant.numOfGrades
    }

    private fun addGrade(restaurant: Restaurant, restaurantGrade: RestaurantGrade): Restaurant {
        return Restaurant(
            id = restaurant.id,
            restaurantAddTicketId = restaurant.restaurantAddTicketId,
            managerId = restaurant.managerId,
            name = restaurant.name,
            country = restaurant.country,
            city = restaurant.city,
            street = restaurant.street,
            building = restaurant.building,
            entrance = restaurant.entrance,
            floor = restaurant.floor,
            description = restaurant.description,
            numOfGrades = restaurant.numOfGrades + 1,
            sumOfGrades = restaurant.sumOfGrades + restaurantGrade.grade,
        )
    }
}