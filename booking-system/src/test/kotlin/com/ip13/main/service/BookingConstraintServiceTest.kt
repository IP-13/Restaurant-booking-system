package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.AddBookingConstraintRequest
import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.repository.BookingConstraintRepository
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
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class BookingConstraintServiceTest {

    @MockK
    private lateinit var restaurantService: RestaurantService

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var bookingConstraintRepository: BookingConstraintRepository

    @InjectMockKs
    private lateinit var bookingConstraintService: BookingConstraintService

    @Test
    fun addCorrectConstraintTest() {
        val dto = AddBookingConstraintRequest(
            TEST_RESTAURANT_ID, "TEST", LocalDateTime.now(),
            LocalDateTime.now().plusDays(1)
        )
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID, manager = User(TEST_USER_ID))
        val defaultUser = User(id = TEST_USER_ID)
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        every { userService.loadUserByUsername(any()) } returns defaultUser
        every { bookingConstraintRepository.save(any()) } returns BookingConstraint(17)
        val bcsReturned = bookingConstraintService.addBookingConstraint(dto, TEST_USERNAME)
        assertAll(
            { Assertions.assertEquals(bcsReturned.id, 17) }
        )
    }

    @Test
    fun addNotAuthorizedConstraintTest() {
        val manager = User(id = TEST_USER_ID)
        val notManager = User(id = TEST_USER_ID + 1)
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID, manager = manager)
        every { userService.findByIdOrNull(TEST_USER_ID) } returns manager
        every { userService.loadUserByUsername(TEST_USERNAME) } returns notManager
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        val dto = AddBookingConstraintRequest(
            TEST_RESTAURANT_ID, "TEST", LocalDateTime.now(),
            LocalDateTime.now().plusDays(1)
        )
        assertThrows<CommonException> {
            bookingConstraintService.addBookingConstraint(dto, TEST_USERNAME)
        }
    }

    companion object {
        const val TEST_RESTAURANT_ID = 11
        const val TEST_USER_ID = 13
        const val TEST_USERNAME = "ip13"
    }
}