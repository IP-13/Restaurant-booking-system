package com.ip13.main.service

import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.TableReserveTicket
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
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

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
        val defaultManager = User(id = TEST_MANAGER_ID)
        val defaultRestaurant1 = Restaurant(id = TEST_RESTAURANT_ID)
        val defaultRestaurant2 = Restaurant(id = TEST_RESTAURANT_ID + 1)
        val defaultTicket1 = TableReserveTicket(
                restaurant = defaultRestaurant1
        )
        val defaultTicket2 = TableReserveTicket(
                restaurant = defaultRestaurant2
        )
        val defaultTicketList = listOf(defaultTicket1, defaultTicket2)
        every { userService.getUserByTokenInHeader(TEST_AUTH_HEADER) } returns defaultManager
        every { restaurantService.findByManagerId(TEST_MANAGER_ID) } returns defaultRestaurant1
        val pageRequest = PageRequest.of(1, 1, Sort.unsorted())
        every { tableReserveTicketRepository.findAll(pageRequest) } returns PageImpl(defaultTicketList)
        val result = tableReserveService.getReservations(TEST_AUTH_HEADER, 1, 1)
        Assertions.assertEquals(1, result.reservations.size)
        Assertions.assertEquals(defaultTicket1.toTableReserveTicketResponse(), result.reservations[0])
    }

    companion object {
        const val TEST_MANAGER_ID = 11
        const val TEST_AUTH_HEADER = "TEST_AUTH_HEADER"
        const val TEST_RESTAURANT_ID = 13
    }
}
