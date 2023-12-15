package com.ip13.main.controller

import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.repository.RestaurantRepository
import com.ip13.main.webClient.blackListClient.BlackListServiceWebClient
import com.ip13.main.webClient.blackListClient.dto.BlackListResponse
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class ReserveControllerTest : AbstractTestContainer() {
    @MockBean
    private lateinit var blackListServiceWebClient: BlackListServiceWebClient

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    private lateinit var restaurantAddTicketRepository: RestaurantAddTicketRepository

    @Test
    @WithMockUser(username = USERNAME)
    fun `reserve table successful`() {
        executeSqlScript("/sql/create_restaurant.sql")

        Mockito.`when`(
            blackListServiceWebClient.getBlackListByUsername(
                USERNAME,
                AUTH_HEADER
            )
        ).thenReturn(listOf())

        val body = loadAsString("/json/reserve-table.json")

        mockMvc.post("/reservation/reserve-table") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", AUTH_HEADER)
        }.andExpect {
            status { isEqualTo(200) }
            content {
                jsonPath(
                    "status",
                    CoreMatchers.containsString(
                        "PROCESSING",
                    )
                )
            }
        }
    }

    @Test
    @WithMockUser(username = USERNAME)
    fun `reserve table in non-existent restaurant`() {
        val body = loadAsString("/json/reserve-table.json")

        mockMvc.post("/reservation/reserve-table") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", AUTH_HEADER)
        }.andExpect {
            status { isEqualTo(404) }
            content {
                jsonPath(
                    "message",
                    CoreMatchers.containsString(
                        "No restaurant with id 100",
                    )
                )
            }
        }
    }

    @Test
    @WithMockUser(username = USERNAME)
    fun `reserve table when in black list`() {
        executeSqlScript("/sql/create_restaurant.sql")

        Mockito.`when`(
            blackListServiceWebClient.getBlackListByUsername(
                USERNAME,
                AUTH_HEADER,
            )
        ).thenReturn(
            listOf(
                BlackListResponse(
                    id = 100,
                    username = USERNAME,
                    fromDate = LocalDateTime.now(),
                    tillDate = LocalDateTime.now(),
                    reason = "toxic"
                )
            )
        )

        val body = loadAsString("/json/reserve-table.json")

        mockMvc.post("/reservation/reserve-table") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", AUTH_HEADER)
        }.andExpect {
            content {
                status { isEqualTo(200) }
                jsonPath(
                    "status",
                    CoreMatchers.containsString(
                        "REJECTED"
                    )
                )
                jsonPath(
                    "managerComment",
                    CoreMatchers.containsString(
                        "You're in a black list for bad behaviour",
                    )
                )
            }
        }
    }

    @Test
    @WithMockUser(username = MANAGER_NAME, authorities = [MANAGER])
    fun processReservationSuccessfullyTest() {
        executeSqlScript("/sql/create_restaurant.sql")
        executeSqlScript("/sql/create-reservation.sql")

        val body = loadAsString("/json/process-reservation.json")

        mockMvc.post("/reservation/process-reservation") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", AUTH_HEADER)
        }.andExpect {
            content {
                status { isEqualTo(200) }
                jsonPath(
                    "status",
                    CoreMatchers.equalTo(
                        "ACCEPTED"
                    )
                )
                jsonPath(
                    "id",
                    CoreMatchers.equalTo(
                        100
                    )
                )
            }
        }
    }

    @Test
    @WithMockUser(username = MANAGER_NAME, authorities = [MANAGER])
    fun `when process already processed reservation should return error in response`() {
        executeSqlScript("/sql/create_restaurant.sql")
        executeSqlScript("/sql/create-processed-reservation.sql")

        val body = loadAsString("/json/process-reservation.json")

        mockMvc.post("/reservation/process-reservation") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", AUTH_HEADER)
        }.andExpect {
            content {
                status { isEqualTo(400) }
                content {
                    json(
                        """
                            {"message":"com.ip13.main.exceptionHandling.exception.CommonException: Reservation with id 100 already processed. Status ACCEPTED"}
                        """.trimIndent(),
                        true
                    )
                }
            }
        }
    }

    @Test
    @WithMockUser(username = MANAGER_NAME, authorities = [MANAGER])
    fun addBookingConstraintTest() {
        executeSqlScript("/sql/create_restaurant.sql")

        val body = loadAsString("/json/add-booking-constraint.json")

        mockMvc.post("/reservation/add-booking-constraint") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", AUTH_HEADER)
        }.andExpect {
            content {
                status { isEqualTo(200) }
                content {
                    json(
                        "{\"id\":100}",
                        true
                    )
                }
            }
        }
    }

    companion object {
        private const val AUTH_HEADER = "Bearer 123"
        private const val USERNAME = "username"
        private const val MANAGER_NAME = "ip13"
    }
}