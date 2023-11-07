package com.ip13.main.service

import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.entity.GradeManager
import com.ip13.main.repository.GradeManagerRepository
import com.ip13.main.security.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime

@Service
class GradeManagerService(
    private val gradeManagerRepository: GradeManagerRepository,
    private val userService: UserService,
    private val blackListService: BlackListService,
    private val transactionManager: PlatformTransactionManager,
) {
    private val transactionTemplate = TransactionTemplate(transactionManager)

    fun gradeUser(gradeManager: GradeManager): Float? {
        // TODO() try-catch
        val newGrade = transactionTemplate.execute {
            gradeManagerRepository.save(gradeManager)
            userService.addGrade(gradeManager.userId, gradeManager.grade)

            val newSumOfGrades = userService.getSumOfGrades(gradeManager.userId)
            val newNumOfGrades = userService.getNumOfGrades(gradeManager.userId)

            val newGrade = (newSumOfGrades.toFloat()) / newNumOfGrades

            if (newGrade < 3.0 && newNumOfGrades > 1) {
                blackListService.save(
                    blackList = BlackList(
                        userId = gradeManager.userId,
                        fromDate = LocalDateTime.now(),
                        tillDate = LocalDateTime.now().plusMonths(3),
                        reason = "grade less than 3.0",
                    )
                )
            }

            newGrade
        }

        return newGrade
    }
}