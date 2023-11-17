package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.GradeVisitorRequest
import com.ip13.main.model.dto.response.GradeVisitorResponse
import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.entity.GradeManager
import com.ip13.main.repository.GradeManagerRepository
import com.ip13.main.security.model.entity.User
import com.ip13.main.security.service.UserService
import com.ip13.main.util.getLogger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class GradeManagerService(
    private val gradeManagerRepository: GradeManagerRepository,
    private val userService: UserService,
    private val blackListService: BlackListService,
    private val tableReserveService: TableReserveService,
) {
    private val log = getLogger(javaClass)

    fun gradeVisitor(authHeader: String, request: GradeVisitorRequest): GradeVisitorResponse {
        val manager = userService.getUserByTokenInHeader(authHeader)

        log.debug("manager extracted from token\n{}", manager.toString())

        val tableReserveTicket = tableReserveService.findByIdOrThrow(request.tableReserveTicketId)

        log.debug("tableReserveTicket loaded from db\n{}", tableReserveTicket.toString())

        if (tableReserveTicket.restaurant.manager.id != manager.id) {
            throw CommonException(
                "You don't work in restaurant ${tableReserveTicket.restaurant.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        if (tableReserveTicket.gradeManager != null) {
            throw CommonException(
                "You already left grade to table reserve ticket with id ${tableReserveTicket.id}",
                HttpStatus.BAD_REQUEST
            )
        }

        val user = tableReserveTicket.user

        val gradeManager = GradeManager(
            manager = manager,
            tableReserveTicket = tableReserveTicket,
            user = tableReserveTicket.user,
            grade = request.grade,
            comment = request.comment,
        )

        val userWithUpdatedGrades = User(
            id = user.id,
            username = user.username,
            password = user.password,
            numOfGrades = user.numOfGrades + 1,
            sumOfGrades = user.sumOfGrades + gradeManager.grade,
        )

        val newAverageGrade = userWithUpdatedGrades.sumOfGrades.toFloat() / userWithUpdatedGrades.numOfGrades

        val newNumOfGrades = userWithUpdatedGrades.numOfGrades

        val isBadPerson = newAverageGrade < 3.0 && newNumOfGrades > 1

        saveGradeManagerAndUpdateUserGradeAndAddToBlackListTransactional(
            gradeManager,
            userWithUpdatedGrades,
            isBadPerson,
        )

        return GradeVisitorResponse(newAverageGrade)
    }

    @Transactional
    private fun saveGradeManagerAndUpdateUserGradeAndAddToBlackListTransactional(
        gradeManager: GradeManager,
        userWithUpdatedGrades: User,
        isBadPerson: Boolean,
    ) {
        gradeManagerRepository.save(gradeManager)

        userService.save(userWithUpdatedGrades)

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