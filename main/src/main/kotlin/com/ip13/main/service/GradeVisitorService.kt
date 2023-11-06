package com.ip13.main.service

import com.ip13.main.model.entity.GradeVisitor
import com.ip13.main.repository.GradeVisitorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Service
class GradeVisitorService(
    private val gradeVisitorRepository: GradeVisitorRepository,
    private val transactionManager: PlatformTransactionManager,
    private val restaurantService: RestaurantService,
) {
    private val transactionTemplate = TransactionTemplate(transactionManager)

    fun save(gradeVisitor: GradeVisitor): Int {
        return gradeVisitorRepository.save(gradeVisitor).id
    }

    fun gradeRestaurant(gradeVisitor: GradeVisitor): Float {
        // TODO() try-catch
        transactionTemplate.execute {
            save(gradeVisitor)
            restaurantService.updateGrade(gradeVisitor.restaurantId, gradeVisitor.grade)
        }

        return restaurantService.getGrade(gradeVisitor.restaurantId)
    }
}