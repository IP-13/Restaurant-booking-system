package com.ip13.main.mapper

import com.ip13.main.provider.EntitiesProvider
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll


class AddressMapperTest {

    @Test
    fun addressFromRestaurantAddTicketTest() {
        val restaurantAddTicket = EntitiesProvider.getDefaultRestaurantAddTicket()

        val address = AddressMapper.addressFromRestaurantAddTicket(restaurantAddTicket)

        assertAll(
            { Assertions.assertThat(address.country).isEqualTo("country") },
            { Assertions.assertThat(address.city).isEqualTo("city") },
            { Assertions.assertThat(address.street).isEqualTo("street") },
            { Assertions.assertThat(address.building).isEqualTo(13) },
            { Assertions.assertThat(address.entrance).isEqualTo(13) },
            { Assertions.assertThat(address.floor).isEqualTo(-2) },
        )
    }
}