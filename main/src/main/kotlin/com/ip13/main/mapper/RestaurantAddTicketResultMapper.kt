package com.ip13.main.mapper

import com.ip13.main.model.dto.RestaurantAddTicketResultDto
import com.ip13.main.model.entity.RestaurantAddTicketResult
import java.time.LocalDateTime

object RestaurantAddTicketResultMapper {
    fun restaurantAddTicketResultFromRestaurantAddTicketResultDto(
        dto: RestaurantAddTicketResultDto
    ): RestaurantAddTicketResult {
        return RestaurantAddTicketResult(
            restaurantAddTicketId = dto.restaurantAddTicketId,
            adminId = dto.adminId,
            result = dto.result,
            createDate = LocalDateTime.now(),
            adminComment = dto.adminComment,
        )
    }
}