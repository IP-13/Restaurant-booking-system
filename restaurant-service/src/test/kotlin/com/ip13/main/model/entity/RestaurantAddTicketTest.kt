package com.ip13.main.model.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class RestaurantAddTicketTest {
    @Test
    fun overridenMethodsTest() {
        val creationDate = LocalDateTime.now()

        val restaurantAddTicket1 = RestaurantAddTicket(creationDate = creationDate)
        val restaurantAddTicket2 = RestaurantAddTicket(creationDate = creationDate)

        Assertions.assertThat(restaurantAddTicket1).isEqualTo(restaurantAddTicket2)
        Assertions.assertThat(restaurantAddTicket1.hashCode()).isEqualTo(restaurantAddTicket2.hashCode())
        Assertions.assertThat(restaurantAddTicket1.toString()).isEqualTo(restaurantAddTicket2.toString())
    }
}