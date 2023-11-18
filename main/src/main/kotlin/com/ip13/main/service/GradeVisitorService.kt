package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.GradeRestaurantRequest
import com.ip13.main.model.entity.RestaurantGrade
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.toRestaurantGrade
import com.ip13.main.repository.GradeVisitorRepository
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GradeVisitorService(
    private val gradeVisitorRepository: GradeVisitorRepository,
    private val restaurantService: RestaurantService,
    private val userService: UserService,
    private val tableReserveService: TableReserveService,
) {

    private val log = getLogger(javaClass)

    fun save(restaurantGrade: RestaurantGrade): Int {
        return gradeVisitorRepository.save(restaurantGrade).id
    }

    fun gradeRestaurant(request: GradeRestaurantRequest, username: String): Float {
        val user = userService.loadUserByUsername(username)

        log.debug("user extracted from token\n{}", user.toString())

        val tableReserveTicket = tableReserveService.findByIdOrThrow(request.tableReserveTicketId)

        log.debug("tableReserveTicket loaded from db\n{}", tableReserveTicket.toString())

        if (tableReserveTicket.user.id != user.id) {
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

        val restaurant = restaurantService.findByIdOrThrow(tableReserveTicket.restaurant.id)

        log.debug("Restaurant loaded from db\n{}", restaurant.toString())

        val restaurantGrade = request.toRestaurantGrade(user, tableReserveTicket, restaurant)

        val updatedRestaurant = addGrade(restaurant, restaurantGrade)

        log.debug("Updated restaurant\n{}", updatedRestaurant)

        saveGradeVisitorAndRestaurantTransactional(updatedRestaurant, restaurantGrade)

        return updatedRestaurant.sumOfGrades.toFloat() / updatedRestaurant.numOfGrades
    }

    private fun addGrade(restaurant: Restaurant, restaurantGrade: RestaurantGrade): Restaurant {
        val updatedGradesFromVisitors = restaurant.gradesFromVisitors.toMutableList()
        updatedGradesFromVisitors.add(restaurantGrade)

        return Restaurant(
            id = restaurant.id,
            restaurantAddTicket = restaurant.restaurantAddTicket,
            manager = restaurant.manager,
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
            tableReserveTickets = restaurant.tableReserveTickets,
            gradesFromVisitors = updatedGradesFromVisitors,
            bookingConstraints = restaurant.bookingConstraints,
        )
    }

    @Transactional
    private fun saveGradeVisitorAndRestaurantTransactional(restaurant: Restaurant, restaurantGrade: RestaurantGrade) {
        save(restaurantGrade)
        restaurantService.save(restaurant)
    }
}