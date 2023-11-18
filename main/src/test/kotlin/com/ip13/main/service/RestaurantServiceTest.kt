package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.RestaurantNotFoundException
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.repository.RestaurantRepository
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
class RestaurantServiceTest {
    @MockK
    private lateinit var restaurantRepository: RestaurantRepository

    @InjectMockKs
    private lateinit var restaurantService: RestaurantService

    @Test
    fun saveRestaurantTest() {
        val defaultRestaurant = Restaurant()
        every { restaurantRepository.save(defaultRestaurant) } returns defaultRestaurant
        Assertions.assertEquals(defaultRestaurant, restaurantService.save(defaultRestaurant))
    }

    @Test
    fun findByIdOrNullTest() {
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID)
        every { restaurantRepository.findByIdOrNull(TEST_RESTAURANT_ID) } returns defaultRestaurant
        every { restaurantRepository.findByIdOrNull(TEST_RESTAURANT_ID + 1) } returns null
        Assertions.assertEquals(defaultRestaurant, restaurantService.findByIdOrNull(TEST_RESTAURANT_ID))
        Assertions.assertNull(restaurantService.findByIdOrNull(TEST_RESTAURANT_ID + 1))
    }

    @Test
    fun findByIdOrThrow() {
        val defaultRestaurant = Restaurant(id = TEST_RESTAURANT_ID)
        every { restaurantRepository.findByIdOrNull(TEST_RESTAURANT_ID) } returns defaultRestaurant
        every { restaurantRepository.findByIdOrNull(TEST_RESTAURANT_ID + 1) } returns null
        Assertions.assertEquals(defaultRestaurant, restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID))
        assertThrows<RestaurantNotFoundException> { restaurantService.findByIdOrThrow(TEST_RESTAURANT_ID + 1) }
    }

    @Test
    fun findByManagerIdOrThrowSuccessfulTest() {
        val defaultRestaurant = Restaurant()
        every { restaurantRepository.findByManagerId(TEST_MANAGER_ID) } returns defaultRestaurant
        Assertions.assertEquals(defaultRestaurant, restaurantService.findByManagerIdOrThrow(TEST_MANAGER_ID))
    }

    @Test
    fun findByManagerIdOrThrowNotExistingTest() {
        every { restaurantRepository.findByManagerId(TEST_MANAGER_ID) } returns null
        assertThrows<RestaurantNotFoundException> { restaurantService.findByManagerIdOrThrow(TEST_MANAGER_ID) }
    }

    companion object {
        const val TEST_RESTAURANT_ID = 11
        const val TEST_MANAGER_ID = 13
    }
}