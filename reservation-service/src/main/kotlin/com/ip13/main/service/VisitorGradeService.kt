package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.RestaurantNotFoundException
import com.ip13.main.feign.restaurantClient.RestaurantClient
import com.ip13.main.feign.userClient.UserClient
import com.ip13.main.feign.userClient.dto.User
import com.ip13.main.model.dto.request.GradeVisitorRequest
import com.ip13.main.model.dto.response.GradeVisitorResponse
import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.entity.VisitorGrade
import com.ip13.main.repository.VisitorGradeRepository
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class VisitorGradeService(
    private val visitorGradeRepository: VisitorGradeRepository,
    private val userClient: UserClient,
    private val restaurantClient: RestaurantClient,
    // TODO black list client
    // private val blackListService: BlackListService,
    private val tableReserveService: TableReserveService,
) {
    private val log = getLogger(javaClass)

    fun gradeVisitor(request: GradeVisitorRequest, username: String, authHeader: String): GradeVisitorResponse {
        val manager = userClient.getUserByUsername(authHeader = authHeader, username = username)

        log.debug("manager extracted from token\n{}", manager.toString())

        val tableReserveTicket = tableReserveService.findByIdOrThrow(request.tableReserveTicketId)

        log.debug("tableReserveTicket loaded from db\n{}", tableReserveTicket.toString())

        val restaurant = restaurantClient.getRestaurantById(authHeader, tableReserveTicket.restaurantId)
            ?: throw RestaurantNotFoundException()

        log.debug("Restaurant received from restaurant-service \n{}", restaurant.toString())

        if (restaurant.managerId != manager.id) {
            throw CommonException(
                "You don't work in restaurant ${restaurant.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        if (tableReserveTicket.visitorGrade != null) {
            throw CommonException(
                "You already left grade to table reserve ticket with id ${tableReserveTicket.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        val user = userClient.getUserByUsername(authHeader = authHeader, username = username)

        val visitorGrade = VisitorGrade(
            managerId = manager.id,
            tableReserveTicket = tableReserveTicket,
            userId = user.id,
            grade = request.grade,
            comment = request.comment,
        )

        val userWithUpdatedGrades = User(
            id = user.id,
            username = user.username,
            numOfGrades = user.numOfGrades + 1,
            sumOfGrades = user.sumOfGrades + visitorGrade.grade,
            roles = user.roles,
        )

        val newAverageGrade = userWithUpdatedGrades.sumOfGrades.toFloat() / userWithUpdatedGrades.numOfGrades

        val newNumOfGrades = userWithUpdatedGrades.numOfGrades

        val isBadPerson = newAverageGrade < 3.0 && newNumOfGrades > 1

        // transaction
        visitorGradeRepository.save(visitorGrade)


        saveGradeManagerAndUpdateUserGradeAndAddToBlackListTransactional(
            visitorGrade,
            userWithUpdatedGrades,
            isBadPerson,
        )

        return GradeVisitorResponse(newAverageGrade)
    }

    @Transactional
    private fun saveGradeManagerAndUpdateUserGradeAndAddToBlackListTransactional(
        visitorGrade: VisitorGrade,
        userWithUpdatedGrades: User,
        isBadPerson: Boolean,
    ) {
        visitorGradeRepository.save(visitorGrade)

        userClient.save(userWithUpdatedGrades)

        if (isBadPerson) {
            blackListService.save(
                blackList = BlackList(
                    user = userWithUpdatedGrades,
                    fromDate = LocalDateTime.now(),
                    tillDate = LocalDateTime.now().plusMonths(3),
                    reason = "grade less than 3.0",
                )
            )
        }
    }
}