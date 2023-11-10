package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.GradeVisitorRequestDto
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

    fun gradeRestaurant(authHeader: String, dto: GradeVisitorRequestDto): Float {
        val user = userService.getUserByTokenInHeader(authHeader)

        log.debug("user extracted from token\n{}", user.toString())

        val tableReserveTicket = tableReserveService.findByIdOrThrow(dto.tableReserveTicketId)

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

        val gradeVisitor = dto.toGradeVisitor(user, tableReserveTicket, restaurant)

        val updatedRestaurant = restaurantService.addGrade(restaurant, gradeVisitor)

        log.debug("Updated restaurant\n{}", updatedRestaurant)

        saveGradeVisitorAndRestaurantTransactional(updatedRestaurant, gradeVisitor)

        return updatedRestaurant.sumOfGrades.toFloat() / updatedRestaurant.numOfGrades
    }

    @Transactional
    private fun saveGradeVisitorAndRestaurantTransactional(restaurant: Restaurant, gradeVisitor: GradeVisitor) {
        save(gradeVisitor)
        restaurantService.save(restaurant)
    }
}