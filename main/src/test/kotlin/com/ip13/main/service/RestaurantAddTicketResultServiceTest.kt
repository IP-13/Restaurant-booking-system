package com.ip13.main.service

import com.ip13.main.provider.EntitiesProvider
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class RestaurantAddTicketResultServiceTest {
    @MockK
    private lateinit var restaurantAddTicketResultRepository: RestaurantAddTicketResultRepository

    @InjectMockKs
    private lateinit var restaurantAddTicketResultService: RestaurantAddTicketResultService

    @Test
    fun saveTest() {
        val restaurantAddTicketResult = EntitiesProvider.getDefaultRestaurantAddTicketResult(id = 13)

        every { restaurantAddTicketResultRepository.save(any()) } returns restaurantAddTicketResult

        Assertions.assertThat(restaurantAddTicketResultService.save(restaurantAddTicketResult)).isEqualTo(13)
    }
}