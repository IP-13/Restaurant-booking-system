package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.TableReserveTicketNotFoundException
import com.ip13.main.model.dto.request.ReservationProcessRequest
import com.ip13.main.model.dto.request.TableReserveRequest
import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.model.enums.TableReserveStatus
import com.ip13.main.model.toTableReserveTicket
import com.ip13.main.model.toTableReserveTicketResponse
import com.ip13.main.repository.TableReserveTicketRepository
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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class TableReserveServiceTest {
    @MockK
    private lateinit var tableReserveTicketRepository: TableReserveTicketRepository

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var restaurantService: RestaurantService

    @InjectMockKs
    private lateinit var tableReserveService: TableReserveService

    @Test
    fun saveTest() {
        val defaultTicket = TableReserveTicket()
        every { tableReserveTicketRepository.save(defaultTicket) } returns defaultTicket
        Assertions.assertEquals(defaultTicket, tableReserveService.save(defaultTicket))
    }

    @Test
    fun getReservationsTest() {
        val defaultManager = User(
                id = TEST_MANAGER_ID,
                username = TEST_USER_NAME
        )
        val defaultRestaurant1 = Restaurant(id = TEST_RESTAURANT_ID)
        val defaultRestaurant2 = Restaurant(id = TEST_RESTAURANT_ID + 1)
        val defaultTicket1 = TableReserveTicket(
                restaurant = defaultRestaurant1
        )
        val defaultTicket2 = TableReserveTicket(
                restaurant = defaultRestaurant2
        )
        val defaultTicketList = listOf(defaultTicket1, defaultTicket2)
        every { userService.loadUserByUsername(TEST_USER_NAME) } returns defaultManager
        every { restaurantService.findByManagerIdOrThrow(TEST_MANAGER_ID) } returns defaultRestaurant1
        val pageRequest = PageRequest.of(1, 1, Sort.unsorted())
        every { tableReserveTicketRepository.findAll(pageRequest) } returns PageImpl(defaultTicketList)
        val result = tableReserveService.getReservations(1, 1, TEST_USER_NAME)
        Assertions.assertEquals(1, result.reservations.size)
        Assertions.assertEquals(defaultTicket1.toTableReserveTicketResponse(), result.reservations[0])
    }

    @Test
    fun findByIdOrNullTest() {
        val defaultTicket = TableReserveTicket(id = TEST_TICKET_ID)
        every { tableReserveTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns defaultTicket
        every { tableReserveTicketRepository.findByIdOrNull(TEST_TICKET_ID + 1) } returns null
        Assertions.assertEquals(defaultTicket, tableReserveService.findByIdOrNull(TEST_TICKET_ID))
        Assertions.assertNull(tableReserveService.findByIdOrNull(TEST_TICKET_ID + 1))
    }

    @Test
    fun findByIdOrThrowTest() {
        val defaultTicket = TableReserveTicket(id = TEST_TICKET_ID)
        every { tableReserveTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns defaultTicket
        every { tableReserveTicketRepository.findByIdOrNull(TEST_TICKET_ID + 1) } returns null
        Assertions.assertEquals(defaultTicket, tableReserveService.findByIdOrThrow(TEST_TICKET_ID))
        assertThrows<TableReserveTicketNotFoundException> { tableReserveService.findByIdOrThrow(TEST_TICKET_ID + 1) }
    }

    @Test
    fun reserveTableSuccessfulTest() {
        val defaultUser = User(
                id = TEST_USER_ID,
                username = TEST_USER_NAME
        )
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID)
        val request = TableReserveRequest(restaurantId = TEST_RESTAURANT_ID)
        val ticket = request.toTableReserveTicket(defaultRestaurant, defaultUser)
        every { tableReserveTicketRepository.save(any()) } returns ticket
        every { userService.loadUserByUsername(TEST_USER_NAME) } returns defaultUser
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        Assertions.assertEquals(TableReserveStatus.PROCESSING, tableReserveService.reserveTable(request, TEST_USER_NAME).status)
    }

    @Test
    fun reserveTableBlackListTest() {
        val defaultUser = User(
                id = TEST_USER_ID,
                username = TEST_USER_NAME,
                blackListEntries = listOf(BlackList(
                    id = 1,
                    fromDate = LocalDateTime.now().minusDays(1),
                    tillDate = LocalDateTime.now().plusDays(1)
                ))
        )
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID)
        val request = TableReserveRequest(restaurantId = TEST_RESTAURANT_ID)
        val ticket = request.toTableReserveTicket(defaultRestaurant, defaultUser)
        every { tableReserveTicketRepository.save(any()) } returns ticket
        every { userService.loadUserByUsername(TEST_USER_NAME) } returns defaultUser
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        Assertions.assertEquals(TableReserveStatus.REJECTED, tableReserveService.reserveTable(request, TEST_USER_NAME).status)
    }

    @Test
    fun reserveTableExpiredBlackListTest() {
        val defaultUser = User(
            id = TEST_USER_ID,
            username = TEST_USER_NAME,
            blackListEntries = listOf(BlackList(
                id = 1,
                fromDate = LocalDateTime.now().minusDays(2),
                tillDate = LocalDateTime.now().minusDays(1)
            ))
        )
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID)
        val request = TableReserveRequest(restaurantId = TEST_RESTAURANT_ID)
        val ticket = request.toTableReserveTicket(defaultRestaurant, defaultUser)
        every { tableReserveTicketRepository.save(any()) } returns ticket
        every { userService.loadUserByUsername(TEST_USER_NAME) } returns defaultUser
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        Assertions.assertEquals(TableReserveStatus.PROCESSING, tableReserveService.reserveTable(request, TEST_USER_NAME).status)
    }

    @Test
    fun reserveTableConstraintTest() {
        val defaultUser = User(
                id = TEST_USER_ID,
                username = TEST_USER_NAME,
        )
        var defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID)
        val constraint = BookingConstraint(
                restaurant = defaultRestaurant,
                fromDate = LocalDateTime.now(),
                tillDate = LocalDateTime.now().plusDays(1)
        )
        defaultRestaurant = Restaurant(
                id = TEST_RESTAURANT_ID,
                bookingConstraints = listOf(constraint)
        )
        val request = TableReserveRequest(restaurantId = TEST_RESTAURANT_ID)
        val ticket = request.toTableReserveTicket(defaultRestaurant, defaultUser)
        every { tableReserveTicketRepository.save(any()) } returns ticket
        every { userService.loadUserByUsername(TEST_USER_NAME) } returns defaultUser
        every { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID) } returns defaultRestaurant
        Assertions.assertEquals(TableReserveStatus.REJECTED, tableReserveService.reserveTable(request, TEST_USER_NAME).status)
    }

    @Test
    fun processReservationSuccessfulTest() {
        val manager = User(
                id = TEST_MANAGER_ID,
                username = TEST_USER_NAME
        )
        val ticket = TableReserveTicket(
                id = TEST_TICKET_ID,
                restaurant = Restaurant(
                        id = TEST_RESTAURANT_ID,
                        manager = manager
                )
        )
        val request = ReservationProcessRequest(
                tableReserveTicketId = TEST_TICKET_ID,
                managerComment = null,
                status = TableReserveStatus.ACCEPTED
        )
        every { userService.loadUserByUsername(TEST_USER_NAME) } returns manager
        every { tableReserveTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns ticket
        every { tableReserveTicketRepository.save(any()) } returns TableReserveTicket()
        val result = tableReserveService.processReservation(request, TEST_USER_NAME)
        Assertions.assertEquals(TableReserveStatus.ACCEPTED, result.status)
    }

    @Test
    fun processReservationNotManagerTest() {
        val manager = User(
                id = TEST_MANAGER_ID,
        )
        val notManager = User(
                id = TEST_USER_ID,
                username = TEST_USER_NAME
        )
        val ticket = TableReserveTicket(
                id = TEST_TICKET_ID,
                restaurant = Restaurant(
                        id = TEST_RESTAURANT_ID,
                        manager = manager
                )
        )
        val request = ReservationProcessRequest(
                tableReserveTicketId = TEST_TICKET_ID,
                managerComment = null,
                status = TableReserveStatus.ACCEPTED
        )
        every { userService.loadUserByUsername(TEST_USER_NAME) } returns notManager
        every { tableReserveTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns ticket
        every { tableReserveTicketRepository.save(any()) } returns TableReserveTicket()
        assertThrows<CommonException> { tableReserveService.processReservation(request, TEST_USER_NAME) }
    }

    @Test
    fun processReservationProcessedTest() {
        val manager = User(
                id = TEST_MANAGER_ID,
                username = TEST_USER_NAME
        )
        val ticket = TableReserveTicket(
                id = TEST_TICKET_ID,
                restaurant = Restaurant(
                        id = TEST_RESTAURANT_ID,
                        manager = manager
                ),
                status = TableReserveStatus.REJECTED
        )
        val request = ReservationProcessRequest(
                tableReserveTicketId = TEST_TICKET_ID,
                managerComment = null,
                status = TableReserveStatus.ACCEPTED
        )
        every { userService.loadUserByUsername(TEST_USER_NAME) } returns manager
        every { tableReserveTicketRepository.findByIdOrNull(TEST_TICKET_ID) } returns ticket
        every { tableReserveTicketRepository.save(any()) } returns TableReserveTicket()
        assertThrows<CommonException> { tableReserveService.processReservation(request, TEST_USER_NAME) }
    }

    companion object {
        const val TEST_MANAGER_ID = 11
        const val TEST_USER_NAME = "TEST_USER_NAME"
        const val TEST_RESTAURANT_ID = 13
        const val TEST_TICKET_ID = 15
        const val TEST_USER_ID = 17
    }
}
