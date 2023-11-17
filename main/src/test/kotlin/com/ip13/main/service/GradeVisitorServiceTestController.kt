package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.GradeVisitorRequest
import com.ip13.main.model.entity.GradeVisitor
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.repository.GradeVisitorRepository
import com.ip13.main.security.model.entity.User
import com.ip13.main.security.service.UserService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GradeVisitorServiceTestController {
    @MockK
    private lateinit var gradeVisitorRepository: GradeVisitorRepository

    @MockK
    private lateinit var restaurantService: RestaurantService

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var tableReserveService: TableReserveService

    @InjectMockKs
    private lateinit var gradeVisitorService: GradeVisitorService

    @Test
    fun saveTest() {
        val defaultGradeVisitor = GradeVisitor(id = TEST_GRADE_VISITOR_ID)
        every { gradeVisitorRepository.save(any()) } returns defaultGradeVisitor
        Assertions.assertEquals(TEST_GRADE_VISITOR_ID, gradeVisitorService.save(defaultGradeVisitor))
    }

    @Test
    fun successfulGradeRestaurantTest() {
        val defaultUser = User(id = TEST_USER_ID)
        val defaultRestaurant = Restaurant(
                id = TEST_RESTAURANT_ID,
                sumOfGrades = TEST_GRADE,
                numOfGrades = 1
        )
        val defaultTableReserveTicket = TableReserveTicket(
                id = TEST_TABLE_RESERVE_ID,
                restaurant = defaultRestaurant,
                user = defaultUser
        )
        val defaultGradeVisitor = GradeVisitor(id = TEST_GRADE_VISITOR_ID)
        val dto = GradeVisitorRequest(
                tableReserveTicketId = defaultTableReserveTicket.id,
                grade = TEST_GRADE,
                comment = TEST_COMMENT
        )
        every { gradeVisitorRepository.save(any()) } returns defaultGradeVisitor
        every { userService.getUserByTokenInHeader(TEST_AUTH_HEADER) } returns defaultUser
        every { tableReserveService.findByIdOrThrow(TEST_TABLE_RESERVE_ID) } returns defaultTableReserveTicket
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        every { restaurantService.save(any()) } returns defaultRestaurant
        Assertions.assertEquals(TEST_GRADE.toFloat(), gradeVisitorService.gradeRestaurant(TEST_AUTH_HEADER, dto))
    }

    @Test
    fun gradeUnvisitedRestaurantTest() {
        val dick = User(id = TEST_USER_ID)
        val visitor = User(id = TEST_USER_ID + 1)
        val defaultRestaurant = Restaurant(
                id = TEST_RESTAURANT_ID,
                sumOfGrades = TEST_GRADE,
                numOfGrades = 1
        )
        val defaultTableReserveTicket = TableReserveTicket(
                id = TEST_TABLE_RESERVE_ID,
                restaurant = defaultRestaurant,
                user = visitor
        )
        val defaultGradeVisitor = GradeVisitor(id = TEST_GRADE_VISITOR_ID)
        val dto = GradeVisitorRequest(
                tableReserveTicketId = defaultTableReserveTicket.id,
                grade = TEST_GRADE,
                comment = TEST_COMMENT
        )
        every { gradeVisitorRepository.save(any()) } returns defaultGradeVisitor
        every { userService.getUserByTokenInHeader(TEST_AUTH_HEADER) } returns dick
        every { tableReserveService.findByIdOrThrow(TEST_TABLE_RESERVE_ID) } returns defaultTableReserveTicket
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        assertThrows<CommonException> { gradeVisitorService.gradeRestaurant(TEST_AUTH_HEADER, dto) }
    }

    @Test
    fun gradeGradedRestaurantTest() {
        val defaultUser = User(id = TEST_USER_ID)
        val defaultRestaurant = Restaurant(
                id = TEST_RESTAURANT_ID,
                sumOfGrades = TEST_GRADE,
                numOfGrades = 1
        )
        var defaultTableReserveTicket = TableReserveTicket(
                id = TEST_TABLE_RESERVE_ID,
                restaurant = defaultRestaurant,
                user = defaultUser
        )
        val defaultGradeVisitor = GradeVisitor(
                id = TEST_GRADE_VISITOR_ID,
                grade = TEST_GRADE,
                tableReserveTicket = defaultTableReserveTicket
        )
        defaultTableReserveTicket = TableReserveTicket(
                id = TEST_TABLE_RESERVE_ID,
                restaurant = defaultRestaurant,
                user = defaultUser,
                gradeVisitor = defaultGradeVisitor
        )
        val dto = GradeVisitorRequest(
                tableReserveTicketId = defaultTableReserveTicket.id,
                grade = TEST_GRADE,
                comment = TEST_COMMENT
        )
        every { gradeVisitorRepository.save(any()) } returns defaultGradeVisitor
        every { userService.getUserByTokenInHeader(TEST_AUTH_HEADER) } returns defaultUser
        every { tableReserveService.findByIdOrThrow(TEST_TABLE_RESERVE_ID) } returns defaultTableReserveTicket
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        assertThrows<CommonException> { gradeVisitorService.gradeRestaurant(TEST_AUTH_HEADER, dto) }
    }

    companion object {
        const val TEST_GRADE_VISITOR_ID = 13
        const val TEST_TABLE_RESERVE_ID = 15
        const val TEST_USER_ID = 17
        const val TEST_RESTAURANT_ID = 11
        const val TEST_GRADE = 5
        const val TEST_AUTH_HEADER = "TEST_AUTH_HEADER"
        const val TEST_COMMENT = "TEST_COMMENT"
    }
}