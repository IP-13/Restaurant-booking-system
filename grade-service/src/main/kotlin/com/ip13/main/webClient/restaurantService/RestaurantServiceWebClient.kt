package com.ip13.main.webClient.restaurantService

import com.ip13.main.webClient.restaurantService.dto.TableReserveTicket
import reactor.core.publisher.Mono

interface RestaurantServiceWebClient {
    fun getTableReserveTicket(id: Int, authHeader: String): Mono<TableReserveTicket>

    suspend fun suspendGetTableReserveTicketOrNull(id: Int, authHeader: String): TableReserveTicket?
}