package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.RestaurantNotFoundException
import com.ip13.main.model.entity.*
import com.ip13.main.repository.RestaurantRepository
import com.ip13.main.security.entity.User
import jakarta.persistence.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
) {
    fun save(restaurant: Restaurant): Restaurant {
        return restaurantRepository.save(restaurant)
    }

    fun findByIdOrNull(id: Int): Restaurant? {
        return restaurantRepository.findByIdOrNull(id)
    }

    fun findByIdOrThrow(id: Int): Restaurant {
        return restaurantRepository.findByIdOrNull(id) ?: throw RestaurantNotFoundException("No restaurant with id $id")
    }

    fun addGrade(restaurant: Restaurant, gradeVisitor: GradeVisitor): Restaurant {
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

    fun getGrade(restaurantId: Int): Float {
        return restaurantRepository.getGrade(restaurantId)
    }

    fun findByManagerId(managerId: Int): Restaurant {
        return restaurantRepository.findByManagerId(managerId)
    }
}