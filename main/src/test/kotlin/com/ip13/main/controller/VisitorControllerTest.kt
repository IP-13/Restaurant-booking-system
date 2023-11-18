package com.ip13.main.controller

import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.TableReserveTicket
import com.ip13.main.model.enums.RestaurantAddStatus
import com.ip13.main.model.enums.Role
import com.ip13.main.model.enums.TableReserveStatus
import com.ip13.main.model.toRestaurant
import com.ip13.main.repository.RestaurantAddTicketRepository
import com.ip13.main.repository.RestaurantGradeRepository
import com.ip13.main.repository.RestaurantRepository
import com.ip13.main.repository.TableReserveTicketRepository
import com.ip13.main.security.model.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

class VisitorControllerTest : AbstractTestContainersTest() {
    @Autowired
    private lateinit var restaurantAddTicketRepository: RestaurantAddTicketRepository

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    private lateinit var tableReserveTicketRepository: TableReserveTicketRepository

    @Autowired
    private lateinit var restaurantGradeRepository: RestaurantGradeRepository

    @Test
    @WithMockUser(username = "ip13")
    fun `should return PROCESSING status on reserve table when no booking constraint and user not in black list`() {
        val user = createDefaultUser()
        createDefaultRestaurant(user)

        val body = loadAsString("json/reserve_table.json")

        mockMvc.post("/visitor/reserve-table") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            MockMvcResultMatchers.status().`is`(200)
            content {
                // проверка что приходит токен
                jsonPath(
                    "status",
                    CoreMatchers.containsString(TableReserveStatus.PROCESSING.name)
                )
            }
        }

        val reservations = tableReserveTicketRepository.findAll()

        assertAll(
            { assertThat(reservations).hasSize(1) },
            { assertThat(reservations.first().userComment).isEqualTo("hello") },
        )
    }

    @Test
    @WithMockUser(username = "ip13")
    fun createTicketTest() {
        createDefaultUser()

        val body = loadAsString("json/create_ticket_to_add_restaurant.json")

        mockMvc.post("/visitor/create-ticket") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            MockMvcResultMatchers.status().`is`(200)
            content {
                // проверка что приходит токен
                jsonPath(
                    "status",
                    CoreMatchers.containsString(TableReserveStatus.PROCESSING.name)
                )
            }
        }

        val restaurantAddTickets = restaurantAddTicketRepository.findAll()

        assertAll(
            { assertThat(restaurantAddTickets).hasSize(1) },
            { assertThat(restaurantAddTickets.first().description).isEqualTo("live long die young") },
        )
    }

    @Test
    @WithMockUser(username = "ip13")
    fun gradeRestaurantTest() {
        val user = createDefaultUser(roles = listOf(Role.MANAGER, Role.ADMIN))
        val restaurant = createDefaultRestaurant(user)

        val tableReserveTicket = TableReserveTicket(
            restaurant = restaurant,
            user = user,
            creationDate = LocalDateTime.now(),
            fromDate = LocalDateTime.now().plusDays(1),
            tillDate = LocalDateTime.now().plusDays(2),
            numOfGuests = 2,
            manager = user,
            status = TableReserveStatus.ACCEPTED,
        )

        tableReserveTicketRepository.save(tableReserveTicket)

        val body = loadAsString("json/grade_restaurant.json")

        mockMvc.post("/visitor/grade-restaurant") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            MockMvcResultMatchers.status().`is`(200)
            content {
                // проверка что приходит токен
                jsonPath(
                    "newGrade",
                    CoreMatchers.equalToObject(2.0)
                )
            }
        }

        val restaurantGrades = restaurantGradeRepository.findAll()

        assertAll(
            { assertThat(restaurantGrades).hasSize(1) },
            { assertThat(restaurantGrades.first().comment).isEqualTo("the food is disgusting") },
        )
    }

    private fun createDefaultRestaurant(user: User): Restaurant {
        val restaurantAddTicket = RestaurantAddTicket(
            name = "Deng",
            country = "USSR",
            city = "Leningrad",
            street = "High-street",
            building = 777,
            entrance = 8,
            floor = null,
            description = "Live long die young",
            user = user,
            creationDate = LocalDateTime.now(),
            status = RestaurantAddStatus.ACCEPTED,
            admin = user,
            processingDate = LocalDateTime.now().plusSeconds(60),
            adminComment = "He is a good man",
        )

        val restaurant = restaurantAddTicket.toRestaurant()

        restaurantAddTicketRepository.save(restaurantAddTicket)
        return restaurantRepository.save(restaurant)
    }
}