package com.ip13.main.mapper

import com.ip13.main.model.entity.enums.RestaurantAddResult
import com.ip13.main.provider.DtoProvider
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class RestaurantAddTicketResultMapperTest {
    @Test
    fun restaurantAddTicketResultFromRestaurantAddTicketResultDto() {
        val restaurantAddTicketResultDto = DtoProvider.getDefaultRestaurantAddTicketResultDto()

        val restaurantAddTicketResult =
            RestaurantAddTicketResultMapper.restaurantAddTicketResultFromRestaurantAddTicketResultDto(
                restaurantAddTicketResultDto
            )

        assertAll(
            { Assertions.assertThat(restaurantAddTicketResult.restaurantAddTicketId).isEqualTo(13) },
            { Assertions.assertThat(restaurantAddTicketResult.adminId).isEqualTo(13) },
            { Assertions.assertThat(restaurantAddTicketResult.result).isEqualTo(RestaurantAddResult.ACCEPTED) },
            { Assertions.assertThat(restaurantAddTicketResult.adminComment).isEqualTo("live long, but die young") },
        )
    }
}