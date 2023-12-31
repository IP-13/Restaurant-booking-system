package com.ip13.main.handler

import com.ip13.main.model.dto.request.RestaurantGradeRequest
import com.ip13.main.model.dto.request.VisitorGradeRequest
import com.ip13.main.repository.RestaurantCoRepository
import com.ip13.main.sql.RunSql
import com.ip13.main.webClient.blackListService.BlackListServiceWebClient
import com.ip13.main.webClient.restaurantService.RestaurantServiceWebClient
import com.ip13.main.webClient.restaurantService.dto.TableReserveStatus
import com.ip13.main.webClient.restaurantService.dto.TableReserveTicket
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

class RestaurantGradeHandlerTest : AbstractTestContainers() {
    @Autowired
    private lateinit var restaurantCoRepository: RestaurantCoRepository

    @MockBean
    lateinit var restaurantServiceWebClient: RestaurantServiceWebClient

    @MockBean
    lateinit var blackListServiceWebClient: BlackListServiceWebClient

    @Test
    @RunSql(["/sql/create_new_tables.sql"])
    @WithMockUser(username = USERNAME)
    fun gradeRestaurantSuccessfulTest() {
        runBlocking {
            Mockito.`when`(
                restaurantServiceWebClient.suspendGetTableReserveTicketOrNull(100, AUTH_HEADER)
            ).thenReturn(
                TableReserveTicket(
                    id = 100,
                    restaurantId = 100,
                    username = USERNAME,
                    creationDate = LocalDateTime.now(),
                    fromDate = LocalDateTime.now(),
                    tillDate = LocalDateTime.now(),
                    numOfGuests = 3,
                    userComment = null,
                    managerName = null,
                    managerComment = null,
                    status = TableReserveStatus.PROCESSING,
                )
            )
        }

        val grade = 3
        val body = RestaurantGradeRequest(100, grade, "toxic")

        val response = webTestClient.post()
            .uri("/grade/restaurant")
            .header("Authorization", AUTH_HEADER)
            .body(Mono.just(body), VisitorGradeRequest::class.java)
            .exchange()
            .expectStatus().isOk
            .expectHeader().valueEquals("Content-Type", "application/x-ndjson")
            .returnResult(Float::class.java).responseBody

        // first grade, new average grade equals to it
        StepVerifier.create(response)
            .expectNext(grade.toFloat())
            .verifyComplete()
    }

    @Test
    @RunSql(["/sql/create_new_tables.sql", "/sql/add_restaurant.sql"])
    @WithMockUser
    fun `get user grade by username`() {
        val restaurantId = 100

        val response = webTestClient.get()
            .uri("/grade/restaurant/$restaurantId")
            .header("Authorization", AUTH_HEADER)
            .exchange()
            .expectStatus().isOk
            .expectHeader().valueEquals("Content-Type", "application/json")
            .returnResult(Float::class.java).responseBody

        StepVerifier.create(response)
            .expectNext(3.75f)
            .verifyComplete()
    }

    companion object {
        private const val AUTH_HEADER = "Bearer 123"
        private const val USERNAME = "ip13"
    }
}