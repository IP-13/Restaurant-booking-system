package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.model.dto.request.AddBookingConstraintRequestDto
import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.toBookingConstraint
import com.ip13.main.repository.BookingConstraintRepository
import com.ip13.main.repository.RestaurantRepository
import com.ip13.main.security.entity.User
import com.ip13.main.security.service.UserService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
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
        val dto = AddBookingConstraintRequestDto(TEST_RESTAURANT_ID, "TEST", LocalDateTime.now(),
                LocalDateTime.now().plusDays(1))
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID, manager = User(TEST_USER_ID))
        val defaultUser = User(id = TEST_USER_ID)
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        every { userService.getUserByTokenInHeader(any()) } returns defaultUser
        every { bookingConstraintRepository.save(any()) } returns BookingConstraint(17)
        val bcsReturned = bookingConstraintService.addBookingConstraint(TEST_AUTH_HEADER, dto)
        assertAll(
                { bcsReturned.id == 17 }
        )
    }

    @Test
    fun addNotAuthorizedConstraintTest() {
        val manager = User(id = TEST_USER_ID)
        val notManager = User(id = TEST_USER_ID + 1)
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID, manager = manager)
        every { userService.findByIdOrNull(TEST_USER_ID) } returns manager
        every { userService.getUserByTokenInHeader(TEST_AUTH_HEADER) } returns notManager
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        val dto = AddBookingConstraintRequestDto(TEST_RESTAURANT_ID, "TEST", LocalDateTime.now(),
                LocalDateTime.now().plusDays(1))
        assertThrows<CommonException> {
            bookingConstraintService.addBookingConstraint(TEST_AUTH_HEADER, dto)
        }
    }

    companion object {
        const val TEST_RESTAURANT_ID = 11;
        const val TEST_USER_ID = 13;
        const val TEST_AUTH_HEADER = "TEST_AUTH_HEADER";
    }
}
