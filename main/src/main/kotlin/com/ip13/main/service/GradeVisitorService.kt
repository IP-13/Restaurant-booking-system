package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.GradeVisitorRequest
import com.ip13.main.model.entity.GradeVisitor
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.toGradeVisitor
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

    fun save(gradeVisitor: GradeVisitor): Int {
        return gradeVisitorRepository.save(gradeVisitor).id
    }

    fun gradeRestaurant(authHeader: String, request: GradeVisitorRequest): Float {
        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("user extracted from token\n{}", user.toString())

        val tableReserveTicket = tableReserveService.findByIdOrThrow(request.tableReserveTicketId)

        log.debug("tableReserveTicket loaded from db\n{}", tableReserveTicket.toString())

        if (tableReserveTicket.user.id != user.id) {
            throw CommonException(
                "You're trying to grade table reserve ticket which doesn't belong to you",
                HttpStatus.BAD_REQUEST
            )
        }

        if (tableReserveTicket.gradeVisitor != null) {
            throw CommonException(
                "You already left grade to table reserve ticket with id ${tableReserveTicket.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        val restaurant = restaurantService.findByIdOrThrow(tableReserveTicket.restaurant.id)

        log.debug("Restaurant loaded from db\n{}", restaurant.toString())

        val gradeVisitor = request.toGradeVisitor(user, tableReserveTicket, restaurant)

        val updatedRestaurant = addGrade(restaurant, gradeVisitor)

        log.debug("Updated restaurant\n{}", updatedRestaurant)

        saveGradeVisitorAndRestaurantTransactional(updatedRestaurant, gradeVisitor)

        return updatedRestaurant.sumOfGrades.toFloat() / updatedRestaurant.numOfGrades
    }

    private fun addGrade(restaurant: Restaurant, gradeVisitor: GradeVisitor): Restaurant {
        val updatedGradesFromVisitors = restaurant.gradesFromVisitors.toMutableList()
        updatedGradesFromVisitors.add(gradeVisitor)

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
            sumOfGrades = restaurant.sumOfGrades + gradeVisitor.grade,
            tableReserveTickets = restaurant.tableReserveTickets,
            gradesFromVisitors = updatedGradesFromVisitors,
            bookingConstraints = restaurant.bookingConstraints,
        )
    }

    @Transactional
    private fun saveGradeVisitorAndRestaurantTransactional(restaurant: Restaurant, gradeVisitor: GradeVisitor) {
        save(gradeVisitor)
        restaurantService.save(restaurant)
    }
}