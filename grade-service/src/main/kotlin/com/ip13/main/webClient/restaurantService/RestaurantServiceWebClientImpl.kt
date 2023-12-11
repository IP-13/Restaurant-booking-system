package com.ip13.main.webClient.restaurantService

import com.ip13.main.webClient.restaurantService.dto.TableReserveTicket
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import reactor.core.publisher.Mono

@Component
class RestaurantServiceWebClientImpl(
    private val restaurantServiceClient: WebClient,
) : RestaurantServiceWebClient {
    override fun getTableReserveTicket(id: Int, authHeader: String): Mono<TableReserveTicket> {
        return restaurantServiceClient
            .get()
            .uri("/reservation/table-reserve-ticket/$id")
            .accept(MediaType.APPLICATION_JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                authHeader,
            )
            .retrieve()
            .bodyToMono(TableReserveTicket::class.java)
    }

    override suspend fun suspendGetTableReserveTicketOrNull(id: Int, authHeader: String) =
        restaurantServiceClient
            .get()
            .uri("/reservation/table-reserve-ticket/$id")
            .accept(MediaType.APPLICATION_JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                authHeader,
            )
            .retrieve()
            .awaitBodyOrNull<TableReserveTicket>()
}