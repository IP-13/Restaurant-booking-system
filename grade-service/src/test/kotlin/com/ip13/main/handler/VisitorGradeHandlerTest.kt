package com.ip13.main.handler

import com.ip13.main.model.dto.request.VisitorGradeRequest
import com.ip13.main.repository.UserRepository
import com.ip13.main.sql.RunSql
import com.ip13.main.webClient.blackListService.BlackListServiceWebClient
import com.ip13.main.webClient.restaurantService.RestaurantServiceWebClient
import com.ip13.main.webClient.restaurantService.dto.TableReserveStatus
import com.ip13.main.webClient.restaurantService.dto.TableReserveTicket
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime


class VisitorGradeHandlerTest : AbstractTestContainers() {
    @Autowired
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var restaurantServiceWebClient: RestaurantServiceWebClient

    @MockBean
    lateinit var blackListServiceWebClient: BlackListServiceWebClient

    @Test
    @RunSql(["/sql/create_new_tables.sql"])
    @WithMockUser(username = MANAGER_NAME)
    fun gradeVisitorSuccessfulTest() {
        Mockito.`when`(
            restaurantServiceWebClient.getTableReserveTicket(100, AUTH_HEADER)
        ).thenReturn(
            Mono.just(
                TableReserveTicket(
                    id = 100,
                    restaurantId = 100,
                    username = USERNAME,
                    creationDate = LocalDateTime.now(),
                    fromDate = LocalDateTime.now(),
                    tillDate = LocalDateTime.now(),
                    numOfGuests = 3,
                    userComment = null,
                    managerName = MANAGER_NAME,
                    managerComment = null,
                    status = TableReserveStatus.PROCESSING,
                )
            )
        )

        val grade = 3

        val body = VisitorGradeRequest(100, grade, "toxic")

        val response = webTestClient.post()
            .uri("/grade/visitor")
            .header("Authorization", AUTH_HEADER)
            .body(Mono.just(body), VisitorGradeRequest::class.java)
            .exchange()
            .expectStatus().isOk
            .expectHeader().valueEquals("Content-Type", "application/json")
            .returnResult(Float::class.java).responseBody

        // first grade, new average grade equals to it
        StepVerifier.create(response)
            .expectNext(grade.toFloat())
            .verifyComplete()
    }
    
    companion object {
        private const val AUTH_HEADER = "Bearer 123456"
        private const val MANAGER_NAME = "ip13"
        private const val USERNAME = "username"
    }
}