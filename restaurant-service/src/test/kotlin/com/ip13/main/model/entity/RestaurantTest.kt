package com.ip13.main.model.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class RestaurantTest {
    @Test
    fun overridenMethodsTest() {
        val restaurantAddTicket = RestaurantAddTicket()

        val restaurant1 = Restaurant(restaurantAddTicket = restaurantAddTicket)
        val restaurant2 = Restaurant(restaurantAddTicket = restaurantAddTicket)

        Assertions.assertThat(restaurant1).isEqualTo(restaurant2)
        Assertions.assertThat(restaurant1.hashCode()).isEqualTo(restaurant2.hashCode())
        Assertions.assertThat(restaurant1.toString()).isEqualTo(restaurant2.toString())
    }
}