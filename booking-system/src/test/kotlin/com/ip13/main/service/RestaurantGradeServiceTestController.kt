package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.GradeRestaurantRequest
import com.ip13.main.model.entity.RestaurantGrade
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.repository.RestaurantGradeRepository
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
class RestaurantGradeServiceTestController {
    @MockK
    private lateinit var restaurantGradeRepository: RestaurantGradeRepository

    @MockK
    private lateinit var restaurantService: RestaurantService

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var tableReserveService: TableReserveService

    @InjectMockKs
    private lateinit var restaurantGradeService: RestaurantGradeService

    @Test
    fun saveTest() {
        val defaultRestaurantGrade = RestaurantGrade(id = TEST_GRADE_VISITOR_ID)
        every { restaurantGradeRepository.save(any()) } returns defaultRestaurantGrade
        Assertions.assertEquals(TEST_GRADE_VISITOR_ID, restaurantGradeService.save(defaultRestaurantGrade))
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
        val defaultRestaurantGrade = RestaurantGrade(id = TEST_GRADE_VISITOR_ID)
        val dto = GradeRestaurantRequest(
            tableReserveTicketId = defaultTableReserveTicket.id,
            grade = TEST_GRADE,
            comment = TEST_COMMENT
        )
        every { restaurantGradeRepository.save(any()) } returns defaultRestaurantGrade
        every { userService.loadUserByUsername(TEST_USERNAME) } returns defaultUser
        every { tableReserveService.findByIdOrThrow(TEST_TABLE_RESERVE_ID) } returns defaultTableReserveTicket
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        every { restaurantService.save(any()) } returns defaultRestaurant
        Assertions.assertEquals(TEST_GRADE.toFloat(), restaurantGradeService.gradeRestaurant(dto, TEST_USERNAME))
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
        val defaultRestaurantGrade = RestaurantGrade(id = TEST_GRADE_VISITOR_ID)
        val dto = GradeRestaurantRequest(
            tableReserveTicketId = defaultTableReserveTicket.id,
            grade = TEST_GRADE,
            comment = TEST_COMMENT
        )
        every { restaurantGradeRepository.save(any()) } returns defaultRestaurantGrade
        every { userService.loadUserByUsername(TEST_USERNAME) } returns dick
        every { tableReserveService.findByIdOrThrow(TEST_TABLE_RESERVE_ID) } returns defaultTableReserveTicket
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        assertThrows<CommonException> { restaurantGradeService.gradeRestaurant(dto, TEST_USERNAME) }
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
        val defaultRestaurantGrade = RestaurantGrade(
            id = TEST_GRADE_VISITOR_ID,
            grade = TEST_GRADE,
            tableReserveTicket = defaultTableReserveTicket
        )
        defaultTableReserveTicket = TableReserveTicket(
            id = TEST_TABLE_RESERVE_ID,
            restaurant = defaultRestaurant,
            user = defaultUser,
            restaurantGrade = defaultRestaurantGrade
        )
        val dto = GradeRestaurantRequest(
            tableReserveTicketId = defaultTableReserveTicket.id,
            grade = TEST_GRADE,
            comment = TEST_COMMENT
        )
        every { restaurantGradeRepository.save(any()) } returns defaultRestaurantGrade
        every { userService.loadUserByUsername(TEST_USERNAME) } returns defaultUser
        every { tableReserveService.findByIdOrThrow(TEST_TABLE_RESERVE_ID) } returns defaultTableReserveTicket
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        assertThrows<CommonException> { restaurantGradeService.gradeRestaurant(dto, TEST_USERNAME) }
    }

    companion object {
        const val TEST_GRADE_VISITOR_ID = 13
        const val TEST_TABLE_RESERVE_ID = 15
        const val TEST_USER_ID = 17
        const val TEST_RESTAURANT_ID = 11
        const val TEST_GRADE = 5
        const val TEST_USERNAME = "ip13"
        const val TEST_COMMENT = "TEST_COMMENT"
    }
}