package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.RestaurantAddTicketNotFoundException
import com.ip13.main.model.dto.request.RestaurantAddTicketRequest
import com.ip13.main.model.dto.request.RestaurantProcessTicketRequest
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.enums.RestaurantAddStatus
import com.ip13.main.model.toRestaurant
import com.ip13.main.repository.RestaurantAddTicketRepository
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
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockKExtension::class)
class RestaurantAddTicketServiceTest {
    @MockK
    private lateinit var restaurantAddTicketRepository: RestaurantAddTicketRepository

    @MockK
    private lateinit var restaurantService: RestaurantService

    @MockK
    private lateinit var userService: UserService

    @InjectMockKs
    private lateinit var restaurantAddTicketService: RestaurantAddTicketService

    @Test
    fun saveTicketTest() {
        val defaultTicket = RestaurantAddTicket()
        every { restaurantAddTicketRepository.save(defaultTicket) } returns defaultTicket
        Assertions.assertEquals(defaultTicket, restaurantAddTicketService.save(defaultTicket))
    }

    @Test
    fun findByIdOrNullTest() {
        val defaultTicket = RestaurantAddTicket(TEST_TICKET_ID)
        every { restaurantAddTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns defaultTicket
        every { restaurantAddTicketRepository.findByIdOrNull(TEST_TICKET_ID + 1) } returns null
        Assertions.assertEquals(defaultTicket, restaurantAddTicketService.findByIdOrNull(TEST_TICKET_ID))
        Assertions.assertNull(restaurantAddTicketService.findByIdOrNull(TEST_TICKET_ID + 1))
    }

    @Test
    fun findByIdOrThrowTest() {
        val defaultTicket = RestaurantAddTicket(TEST_TICKET_ID)
        every { restaurantAddTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns defaultTicket
        every { restaurantAddTicketRepository.findByIdOrNull(TEST_TICKET_ID + 1) } returns null
        Assertions.assertEquals(defaultTicket, restaurantAddTicketService.findByIdOrNull(TEST_TICKET_ID))
        assertThrows<RestaurantAddTicketNotFoundException> { restaurantAddTicketService.findByIdOrThrow(TEST_TICKET_ID + 1) }
    }

    @Test
    fun createTicketTest() {
        val defaultUser = User(id = TEST_USER_ID)
        val defaultTicket = RestaurantAddTicket(
                id = TEST_TICKET_ID,
                user = defaultUser
        )
        val requestDTO = RestaurantAddTicketRequest(
                name = TEST_RESTAURANT_NAME,
                country = TEST_RESTAURANT_COUNTRY,
                city = TEST_RESTAURANT_CITY,
                street = TEST_RESTAURANT_STREET,
                building = TEST_RESTAURANT_BUILDING,
                entrance = TEST_RESTAURANT_ENTRANCE,
                floor = TEST_RESTAURANT_FLOOR
        )
        every { userService.getUserByTokenInHeader(TEST_AUTH_HEADER) } returns defaultUser
        every { restaurantAddTicketRepository.save(any()) } returns defaultTicket
        val responseDTO = restaurantAddTicketService.createTicket(TEST_AUTH_HEADER, requestDTO)
        Assertions.assertEquals(RestaurantAddStatus.PROCESSING, responseDTO.status)
    }

    @Test
    fun notNewUpdateStatusTest() {
        val defaultUser = User(id = TEST_USER_ID)
        val defaultTicket = RestaurantAddTicket(
                id = TEST_TICKET_ID,
                user = defaultUser,
                status = RestaurantAddStatus.REJECTED
        )
        val dto = RestaurantProcessTicketRequest(
                restaurantAddTicketId = TEST_TICKET_ID,
                status = RestaurantAddStatus.PROCESSING,
                adminComment = null
        )
        every { restaurantAddTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns defaultTicket
        assertThrows<CommonException> { restaurantAddTicketService.processRestaurantAddTicket(TEST_AUTH_HEADER, dto) }
    }

    @Test
    fun successfulUpdateStatusAcceptedTest() {
        val defaultUser = User(id = TEST_USER_ID)
        val defaultTicket = RestaurantAddTicket(
                id = TEST_TICKET_ID,
                user = defaultUser
        )
        val modifiedTicket = RestaurantAddTicket(
                id = TEST_TICKET_ID,
                user = defaultUser,
                status = RestaurantAddStatus.ACCEPTED
        )
        val dto = RestaurantProcessTicketRequest(
                restaurantAddTicketId = TEST_TICKET_ID,
                status = RestaurantAddStatus.ACCEPTED,
                adminComment = null
        )
        every { userService.getUserByTokenInHeader(TEST_AUTH_HEADER) } returns defaultUser
        every { userService.addRole(any()) } returns true
        every { restaurantService.save(any()) } returns defaultTicket.toRestaurant()
        every { restaurantAddTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns defaultTicket
        every { restaurantAddTicketRepository.save(any()) } returns modifiedTicket
        every { restaurantService.save(defaultTicket.toRestaurant()) } returns defaultTicket.toRestaurant()
        val response = restaurantAddTicketService.processRestaurantAddTicket(TEST_AUTH_HEADER, dto)
        Assertions.assertEquals(RestaurantAddStatus.ACCEPTED, response.status)
        Assertions.assertEquals(defaultTicket.toRestaurant().id, response.newRestaurantId)
    }

    @Test
    fun successfulUpdateStatusRejectedTest() {
        val defaultUser = User(id = TEST_USER_ID)
        val defaultTicket = RestaurantAddTicket(
                id = TEST_TICKET_ID,
                user = defaultUser
        )
        val modifiedTicket = RestaurantAddTicket(
                id = TEST_TICKET_ID,
                user = defaultUser,
                status = RestaurantAddStatus.REJECTED
        )
        val dto = RestaurantProcessTicketRequest(
                restaurantAddTicketId = TEST_TICKET_ID,
                status = RestaurantAddStatus.REJECTED,
                adminComment = null
        )
        every { userService.getUserByTokenInHeader(TEST_AUTH_HEADER) } returns defaultUser
        every { userService.addRole(any()) } returns true
        every { restaurantAddTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns defaultTicket
        every { restaurantAddTicketRepository.save(any()) } returns modifiedTicket
        val response = restaurantAddTicketService.processRestaurantAddTicket(TEST_AUTH_HEADER, dto)
        Assertions.assertEquals(RestaurantAddStatus.REJECTED, response.status)
        Assertions.assertNull(response.newRestaurantId)
    }

    companion object {
        const val TEST_TICKET_ID = 13
        const val TEST_USER_ID = 17
        const val TEST_RESTAURANT_CITY = "TEST_RESTAURANT_CITY"
        const val TEST_RESTAURANT_COUNTRY = "TEST_RESTAURANT_COUNTRY"
        const val TEST_RESTAURANT_STREET = "TEST_RESTAURANT_STREET"
        const val TEST_RESTAURANT_NAME = "TEST_RESTAURANT_NAME"
        const val TEST_RESTAURANT_BUILDING = 42
        const val TEST_RESTAURANT_ENTRANCE = 7
        const val TEST_RESTAURANT_FLOOR = 2
        const val TEST_AUTH_HEADER = "TEST_AUTH_HEADER"
    }
}
