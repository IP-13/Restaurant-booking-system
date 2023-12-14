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
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import reactor.core.publisher.Mono
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
//        runBlocking {
//            Mockito.`when`(
//                restaurantServiceWebClient.suspendGetTableReserveTicketOrNull(100, AUTH_HEADER)
//            ).thenReturn(
//                TableReserveTicket(
//                    id = 100,
//                    restaurantId = 100,
//                    username = USERNAME,
//                    creationDate = LocalDateTime.now(),
//                    fromDate = LocalDateTime.now(),
//                    tillDate = LocalDateTime.now(),
//                    numOfGuests = 3,
//                    userComment = null,
//                    managerName = null,
//                    managerComment = null,
//                    status = TableReserveStatus.PROCESSING,
//                )
//            )
//        }

        val body = loadAsString("/json/grade-visitor.json")

        val response = webTestClient.post()
            .uri("/grade/restaurant")
//            .header("Authorization", AUTH_HEADER)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(RestaurantGradeRequest(100, 3, "toxic")), VisitorGradeRequest::class.java)
            .exchange()
//            .expectStatus().isOk
            .expectBody().json("{\"grade\": 4}")

//            .returnResult(Float::class.java)
//            .responseBody

        println("_____________________________________________________________________________")
//        response.subscribe(::println)
        println("_____________________________________________________________________________")
    }

    companion object {
        private const val AUTH_HEADER = "Bearer 123"
        private const val USERNAME = "ip13"
    }
}