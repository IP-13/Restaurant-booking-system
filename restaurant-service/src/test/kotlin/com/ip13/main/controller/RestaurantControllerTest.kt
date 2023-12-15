package com.ip13.main.controller

import com.ip13.main.feign.userClient.UserClient
import com.ip13.main.feign.userClient.dto.RoleAddRequest
import com.ip13.main.model.enums.Role
import com.ip13.main.repository.RestaurantAddTicketRepository
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import javax.sql.DataSource


@ExtendWith(MockitoExtension::class)
class RestaurantControllerTest : AbstractTestContainer() {
    @Autowired
    lateinit var restaurantAddTicketRepository: RestaurantAddTicketRepository

    @MockBean
    lateinit var userClient: UserClient

    @Test
    @WithMockUser
    fun createTicketTest() {
        val body = loadAsString("/json/create-ticket.json")

        mockMvc.post("/restaurant/create-ticket") {
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
    @WithMockUser(authorities = [ADMIN], username = USERNAME)
    fun processTicketTest() {
        executeSqlScript("/sql/create_restaurant_add_ticket.sql")

        Mockito.`when`(
            userClient.addRole(
                authHeader = AUTH_HEADER,
                roleAddRequest = RoleAddRequest(
                    username = USERNAME,
                    role = Role.MANAGER,
                )
            )
        ).thenReturn(
            "OK"
        )

        val body = loadAsString("/json/process-ticket.json")

        mockMvc.post("/restaurant/process-ticket") {
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
                        "ACCEPTED",
                    )
                )
                jsonPath(
                    "newRestaurantId",
                    CoreMatchers.equalTo(
                        100
                    )
                )
            }
        }
    }

    @Test
    @WithMockUser
    fun getByIdTest() {
        executeSqlScript("/sql/create_restaurant.sql")

        mockMvc.get("/restaurant/id/100") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header("Authorization", AUTH_HEADER)
        }.andExpect {
            status { isEqualTo(200) }
            content {
                json(
                    """
                        {
                            "id":100,
                            "restaurantAddTicketId":100,
                            "managerName":"ip13",
                            "name":"codex",
                            "country":"Russia",
                            "city":"Saint-Petersburg",
                            "street":"Windy",
                            "building":1,
                            "entrance":4,
                            "floor":3,
                            "description":null
                        }
                    """.trimIndent(),
                    true
                )
            }
        }
    }

    companion object {
        private const val AUTH_HEADER = "Bearer 123"
        private const val USERNAME = "ip13"
    }
}