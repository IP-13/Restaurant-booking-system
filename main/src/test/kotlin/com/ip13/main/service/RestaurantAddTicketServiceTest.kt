package com.ip13.main.service


import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.enums.RestaurantAddStatus
import com.ip13.main.model.entity.enums.Role
import com.ip13.main.provider.EntitiesProvider
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.security.service.UserService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull


@ExtendWith(MockKExtension::class)
class RestaurantAddTicketServiceTest {
    @MockK
    private lateinit var restaurantAddTicketRepository: RestaurantAddTicketRepository

    @MockK
    private lateinit var restaurantAddTicketResultService: RestaurantAddTicketResultService

    @MockK
    private lateinit var restaurantService: RestaurantService

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var managerService: ManagerService

    @InjectMockKs
    private lateinit var restaurantAddTicketService: RestaurantAddTicketService

    @Test
    fun saveTest() {
        val restaurantAddTicket = EntitiesProvider.getDefaultRestaurantAddTicket(id = 11)

        every { restaurantAddTicketRepository.save(any()) } returns restaurantAddTicket

        val restaurantAddTicketId = restaurantAddTicketService.save(RestaurantAddTicket())

        Assertions.assertThat(restaurantAddTicketId).isEqualTo(11)
        verify(exactly = 1) { restaurantAddTicketRepository.save(any()) }
    }

    @Test
    fun findByIdTest() {
        val restaurantAddTicket = EntitiesProvider.getDefaultRestaurantAddTicket()

        every { restaurantAddTicketRepository.findByIdOrNull(any()) } returns restaurantAddTicket

        Assertions.assertThat(restaurantAddTicketService.findByIdOrNull(10)).isEqualTo(restaurantAddTicket)
        verify(exactly = 1) { restaurantAddTicketRepository.findByIdOrNull(any()) }
    }

    @Test
    fun `findById should return null when restaurantAddTicketRepository returns null`() {
        every { restaurantAddTicketRepository.findByIdOrNull(any()) } returns null

        Assertions.assertThat(restaurantAddTicketService.findByIdOrNull(10)).isEqualTo(null)
        verify(exactly = 1) { restaurantAddTicketRepository.findByIdOrNull(any()) }
    }

    @Test
    fun `successful processRestaurantAddTicket`() {
        val result = EntitiesProvider.getDefaultRestaurantAddTicketResult(
            id = 13,
            result = RestaurantAddStatus.ACCEPTED
        )
        val ticket = EntitiesProvider.getDefaultRestaurantAddTicket()

        every { restaurantAddTicketResultService.save(result) } returns result.id
        every { userService.addRole(any(), Role.MANAGER.name) } returns true
        // returns id of new added restaurant
        every { restaurantService.save(any()) } returns 13
        every { managerService.save(any()) } returns 13

        Assertions.assertThat(restaurantAddTicketService.processRestaurantAddTicket(result, ticket)).isEqualTo(13)
        verify(exactly = 1) { restaurantAddTicketResultService.save(any()) }
        verify(exactly = 1) { userService.addRole(any(), any()) }
        verify(exactly = 1) { restaurantService.save(any()) }
    }

    @Test
    fun `should save ticketResult and return null when rejected`() {
        val result = EntitiesProvider.getDefaultRestaurantAddTicketResult(
            id = 13,
            result = RestaurantAddStatus.REJECTED
        )
        val ticket = EntitiesProvider.getDefaultRestaurantAddTicket()

        every { restaurantAddTicketResultService.save(result) } returns result.id

        Assertions.assertThat(restaurantAddTicketService.processRestaurantAddTicket(result, ticket)).isEqualTo(null)
        verify(exactly = 1) { restaurantAddTicketResultService.save(any()) }
    }

    @Test
    fun getTicketsTest() {
        val pageRequest = PageRequest.of(0, 10, Sort.unsorted())

        every { restaurantAddTicketRepository.findAll(pageRequest) } returns PageImpl(
            listOf(
                EntitiesProvider.getDefaultRestaurantAddTicket(id = 1, name = "first"),
                EntitiesProvider.getDefaultRestaurantAddTicket(id = 13, name = "second"),
                EntitiesProvider.getDefaultRestaurantAddTicket(id = 1313, name = "third"),
                EntitiesProvider.getDefaultRestaurantAddTicket(id = 131313, name = "forth"),
            )
        )

        val tickets = restaurantAddTicketService.getTickets(pageRequest)

        assertAll(
            { Assertions.assertThat(tickets).hasSize(4) },
            { Assertions.assertThat(tickets[0].name).isEqualTo("first") },
            { Assertions.assertThat(tickets[1].name).isEqualTo("second") },
            { Assertions.assertThat(tickets[2].name).isEqualTo("third") },
            { Assertions.assertThat(tickets[3].name).isEqualTo("forth") },
        )
    }
}