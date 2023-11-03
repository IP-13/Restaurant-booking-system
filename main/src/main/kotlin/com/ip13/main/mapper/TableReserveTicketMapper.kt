package com.ip13.main.mapper

import com.ip13.main.model.dto.TableReserveTicketDto
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.security.entity.User
import java.time.LocalDateTime

object TableReserveTicketMapper {
    fun tableReserveTicketDtoToTableReserveTicket(tableReserveTicketDto: TableReserveTicketDto): TableReserveTicket {
        return TableReserveTicket(
            restaurant = Restaurant(id = tableReserveTicketDto.restaurantId),
            user = User(id = tableReserveTicketDto.userId),
            userComment = tableReserveTicketDto.userComment,
            creationDate = LocalDateTime.now(),
        )
    }
}