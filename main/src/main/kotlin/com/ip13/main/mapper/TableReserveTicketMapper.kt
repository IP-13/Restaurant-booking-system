package com.ip13.main.mapper

import com.ip13.main.model.dto.TableReserveTicketDto
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.security.entity.User
import java.time.LocalDateTime

object TableReserveTicketMapper {
    fun fromTableReserveTicketDto(tableReserveTicketDto: TableReserveTicketDto): TableReserveTicket {
        return TableReserveTicket(
            restaurantId = tableReserveTicketDto.restaurantId,
            userId = tableReserveTicketDto.userId,
            fromDate = tableReserveTicketDto.fromDate,
            tillDate = tableReserveTicketDto.tillDate,
            numOfGuests = tableReserveTicketDto.numOfGuests,
            userComment = tableReserveTicketDto.userComment,
        )
    }
}