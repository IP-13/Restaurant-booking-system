package com.ip13.main.mapper

import com.ip13.main.model.entity.Address
import com.ip13.main.model.entity.Manager
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.provider.EntitiesProvider
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDateTime
import java.time.Month

class RestaurantMapperTest {
    @Test
    fun restaurantFromRestaurantAddTicketTest() {
        val restaurantAddTicket = EntitiesProvider.getDefaultRestaurantAddTicket()

        val restaurant = RestaurantMapper.restaurantFromRestaurantAddTicket(restaurantAddTicket)

        assertAll(
            { Assertions.assertThat(restaurant.id).isEqualTo(0) },
            { Assertions.assertThat(restaurant.name).isEqualTo("restaurant") },
            {
                Assertions.assertThat(restaurant.address).isEqualTo(
                    Address(
                        id = 0,
                        country = "country",
                        city = "city",
                        street = "street",
                        building = 13,
                        entrance = 13,
                        floor = -2,
                    )
                )
            },
            {
                Assertions.assertThat(restaurant.restaurantAddTicket).isEqualTo(
                    RestaurantAddTicket(
                        id = 13,
                        name = "restaurant",
                        country = "country",
                        city = "city",
                        street = "street",
                        building = 13,
                        entrance = 13,
                        floor = -2,
                        description = "this is a restaurant in the parking lot",
                        userId = 13,
                        createDate = LocalDateTime.of(
                            2002,
                            Month.APRIL,
                            19,
                            13,
                            13
                        )
                    )
                )
            },
            { Assertions.assertThat(restaurant.description).isEqualTo("this is a restaurant in the parking lot") },
            { Assertions.assertThat(restaurant.managers).isEqualTo(listOf<Manager>()) },
        )
    }
}