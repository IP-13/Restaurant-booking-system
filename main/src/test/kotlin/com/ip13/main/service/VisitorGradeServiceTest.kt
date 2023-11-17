package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.GradeVisitorRequest
import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.entity.VisitorGrade
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.repository.GradeManagerRepository
import com.ip13.main.security.model.entity.User
import com.ip13.main.security.service.UserService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class VisitorGradeServiceTest {
    @MockK
    private lateinit var gradeManagerRepository: GradeManagerRepository

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var blackListService: BlackListService

    @MockK
    private lateinit var tableReserveService: TableReserveService

    @InjectMockKs
    private lateinit var gradeManagerService: GradeManagerService

    @Test
    fun successfulGradeUserTest() {
        val defaultUser = User(id = BookingConstraintServiceTest.TEST_USER_ID)
        val defaultManager = User(id = TEST_MANAGER_ID)
        val defaultRestaurant = Restaurant(id = BookingConstraintServiceTest.TEST_RESTAURANT_ID, manager = defaultManager)
        val defaultTableReserveTicket = TableReserveTicket(
                id = TEST_TABLE_RESERVE_ID,
                restaurant = defaultRestaurant,
                user = defaultUser
        )
        val defaultVisitorGrade = VisitorGrade(
                manager = defaultManager,
                user = defaultUser,
                tableReserveTicket = defaultTableReserveTicket,
                grade = TEST_GRADE
        )
        val defaultBlackList = BlackList(user = defaultUser)
        val dto = GradeVisitorRequest(
                tableReserveTicketId = defaultTableReserveTicket.id,
                grade = TEST_GRADE,
                comment = TEST_COMMENT
        )
        every { userService.getUserByTokenInHeader(any()) } returns defaultManager
        every { userService.save(any()) } returns defaultUser
        every { tableReserveService.findByIdOrThrow(any()) } returns defaultTableReserveTicket
        every { gradeManagerRepository.save(any()) } returns defaultVisitorGrade
        every { blackListService.save(any()) } returns defaultBlackList.id
        val response = gradeManagerService.gradeVisitor(TEST_AUTH_HEADER, dto)
        assertAll(
                { Assertions.assertEquals(TEST_GRADE.toFloat(), response.newAverageGrade) }
        )
    }

    @Test
    fun unauthorizedGradeUserTest() {
        val defaultUser = User(id = BookingConstraintServiceTest.TEST_USER_ID)
        val defaultManager = User(id = TEST_MANAGER_ID)
        val defaultRestaurant = Restaurant(id = BookingConstraintServiceTest.TEST_RESTAURANT_ID, manager = defaultManager)
        val defaultTableReserveTicket = TableReserveTicket(
                id = TEST_TABLE_RESERVE_ID,
                restaurant = defaultRestaurant,
                user = defaultUser
        )
        val dto = GradeVisitorRequest(
                tableReserveTicketId = defaultTableReserveTicket.id,
                grade = TEST_GRADE,
                comment = TEST_COMMENT
        )
        every { userService.getUserByTokenInHeader(any()) } returns User(TEST_MANAGER_ID + 1)
        every { tableReserveService.findByIdOrThrow(any()) } returns defaultTableReserveTicket
        assertThrows<CommonException> { gradeManagerService.gradeVisitor(TEST_AUTH_HEADER, dto) }
    }

    @Test
    fun doubleGradeUserTest() {
        val defaultUser = User(id = BookingConstraintServiceTest.TEST_USER_ID)
        val defaultManager = User(id = TEST_MANAGER_ID)
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID, manager = defaultManager)
        var defaultTableReserveTicket = TableReserveTicket(
                id = TEST_TABLE_RESERVE_ID,
                restaurant = defaultRestaurant,
                user = defaultUser
        )
        val defaultVisitorGrade = VisitorGrade(
                manager = defaultManager,
                user = defaultUser,
                tableReserveTicket = defaultTableReserveTicket,
                grade = TEST_GRADE
        )
        defaultTableReserveTicket = TableReserveTicket(
                id = TEST_TABLE_RESERVE_ID,
                restaurant = defaultRestaurant,
                user = defaultUser,
                visitorGrade = defaultVisitorGrade
        )
        val dto = GradeVisitorRequest(
                tableReserveTicketId = defaultTableReserveTicket.id,
                grade = TEST_GRADE,
                comment = TEST_COMMENT
        )
        every { userService.getUserByTokenInHeader(any()) } returns defaultManager
        every { userService.save(any()) } returns defaultUser
        every { tableReserveService.findByIdOrThrow(any()) } returns defaultTableReserveTicket
        every { gradeManagerRepository.save(any()) } returns defaultVisitorGrade
        assertThrows<CommonException> { gradeManagerService.gradeVisitor(TEST_AUTH_HEADER, dto) }
    }

    companion object {
        const val TEST_TABLE_RESERVE_ID = 15
        const val TEST_MANAGER_ID = 17
        const val TEST_GRADE = 5
        const val TEST_RESTAURANT_ID = 11
        const val TEST_AUTH_HEADER = "TEST_AUTH_HEADER"
        const val TEST_COMMENT = "TEST_COMMENT"
    }
}