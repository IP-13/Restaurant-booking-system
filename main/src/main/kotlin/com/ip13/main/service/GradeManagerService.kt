package com.ip13.main.service

import com.ip13.main.model.entity.GradeManager
import com.ip13.main.repository.GradeManagerRepository
import com.ip13.main.security.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Service
class GradeManagerService(
    private val gradeManagerRepository: GradeManagerRepository,
    private val userRepository: UserRepository,
    private val transactionManager: PlatformTransactionManager,
) {
    private val transactionTemplate = TransactionTemplate(transactionManager)

    fun gradeUser(gradeManager: GradeManager): Float {
        transactionTemplate.execute {
            gradeManagerRepository.save(gradeManager)
            userRepository.addGrade(gradeManager.userId, gradeManager.grade)
        }

        return userRepository.getGrade(gradeManager.userId)
    }
}