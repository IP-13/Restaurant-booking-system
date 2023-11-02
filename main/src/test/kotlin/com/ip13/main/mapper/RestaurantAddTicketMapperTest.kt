package com.ip13.main.mapper

import com.ip13.main.provider.DtoProvider
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class RestaurantAddTicketMapperTest {

    @Test
    fun restaurantAddTicketDtoToRestaurantAddTicketTest() {
        val restaurantAddTicketDto = DtoProvider.getDefaultRestaurantAddTicketDto()

        val restaurantAddTicket =
            RestaurantAddTicketMapper.restaurantAddTicketDtoToRestaurantAddTicket(restaurantAddTicketDto)

        assertAll(
            { Assertions.assertThat(restaurantAddTicket.name).isEqualTo("restaurant name") },
            { Assertions.assertThat(restaurantAddTicket.country).isEqualTo("country") },
            { Assertions.assertThat(restaurantAddTicket.city).isEqualTo("city") },
            { Assertions.assertThat(restaurantAddTicket.street).isEqualTo("street") },
            { Assertions.assertThat(restaurantAddTicket.building).isEqualTo(13) },
            { Assertions.assertThat(restaurantAddTicket.entrance).isEqualTo(13) },
            { Assertions.assertThat(restaurantAddTicket.floor).isEqualTo(-2) },
            {
                Assertions.assertThat(restaurantAddTicket.description)
                    .isEqualTo("this is a restaurant from parking lot")
            },
            { Assertions.assertThat(restaurantAddTicket.userId).isEqualTo(13) },
        )
    }
}