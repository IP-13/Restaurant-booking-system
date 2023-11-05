package com.ip13.main.service

import com.ip13.main.provider.EntitiesProvider
import com.ip13.main.repository.RestaurantRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class RestaurantServiceTest {
    @MockK
    private lateinit var restaurantRepository: RestaurantRepository

    @InjectMockKs
    private lateinit var restaurantService: RestaurantService

    @Test
    fun saveTest() {
        val restaurant = EntitiesProvider.getDefaultRestaurant()

        every { restaurantRepository.save(restaurant) } returns EntitiesProvider.getDefaultRestaurant(id = 13)

        Assertions.assertThat(restaurantService.save(restaurant)).isEqualTo(13)
        verify(exactly = 1) { restaurantRepository.save(any()) }
    }
}