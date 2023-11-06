package com.ip13.main

import com.ip13.main.model.entity.enums.Role
import com.ip13.main.security.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["security.enabled=true"],
)
@ActiveProfiles("test-containers-flyway")
@Testcontainers
@AutoConfigureMockMvc
class EndPointsOnWholeSystemTest(
    @Autowired val jdbc: NamedParameterJdbcTemplate,
    @Autowired val userRepository: UserRepository,
    @Autowired val passwordEncoder: PasswordEncoder,
) {
    @AfterEach
    fun cleanUp() {
        jdbc.execute("truncate table black_list cascade") { _ -> { } }
        jdbc.execute("truncate table booking_constraint cascade") { _ -> { } }
        jdbc.execute("truncate table grade_after_visit_manager cascade") { _ -> { } }
        jdbc.execute("truncate table grade_after_visit_visitor cascade") { _ -> { } }
        jdbc.execute("truncate table table_reserve_ticket_result cascade") { _ -> { } }
        jdbc.execute("truncate table table_reserve_ticket cascade") { _ -> { } }
        jdbc.execute("truncate table manager cascade") { _ -> { } }
        jdbc.execute("truncate table restaurant cascade") { _ -> { } }
        jdbc.execute("truncate table restaurant_add_ticket_result cascade") { _ -> { } }
        jdbc.execute("truncate table restaurant_add_ticket cascade") { _ -> { } }
        jdbc.execute("truncate table address cascade") { _ -> { } }
        // delete all admins except mega_admin
        jdbc.execute("delete from admin where id != 100") { _ -> { } }
        // delete all users except mega_admin
        jdbc.execute("delete from user_t where id != 100") { _ -> { } }
    }

    @Test
    fun `container is running test`() {
        println(container.databaseName)
    }

    @Test
    fun `test successful insert and select with address table`() {
        jdbc.update(
            "insert into address(country, city, street, building, entrance, floor) " +
                    "values('Russia', 'Saint-Petersburg', 'Lenina', 12, 1, 3)",
            mapOf<String, Any>()
        )

        val address = jdbc.query("select * from address") { rs, _ ->
            rs.getString("country") +
                    rs.getString("city") +
                    rs.getString("street") +
                    rs.getInt("building") +
                    rs.getInt("entrance") +
                    rs.getInt("floor")
        }.first()

        assertThat(address).isEqualTo("RussiaSaint-PetersburgLenina1213")
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should add new user to db when register successfully`() {
        val body = loadAsString("json/default_user_register_dto.json")

        mockMvc.post("/security/register") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status().`is`(200)
            content {
                // проверка что приходит токен
                jsonPath(
                    "token",
                    containsString(""),
                )
            }
        }

        val user = userRepository.findByUsername("ip13")!!

        assertAll(
            { assertThat(passwordEncoder.matches("who am i", user.password)).isTrue() },
            { assertThat(user.username).isEqualTo("ip13") },
            { assertThat(user.numOfGrades).isEqualTo(0) },
            { assertThat(user.sumOfGrades).isEqualTo(0) },
            { assertThat(user.roles).isEqualTo(listOf<Role>()) },
        )
    }

//    fun `should return 400 status code when someone tries to register with username that already exists`() {
//
//    }

    @Test
    @WithMockUser(authorities = [ADMIN])
    fun `test add role to non-existent user`() {
        val body = loadAsString("json/non_existent_user.json")

        mockMvc.post("/admin/add_role") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status().`is`(400)
            content {
                jsonPath(
                    "message",
                    containsString(
                        "UserNotFoundException: User with id: 10 not found",
                    )
                )
            }
        }
    }

    companion object {
        private const val POSTGRES_IMAGE = "postgres:16.0"

        private const val ADMIN = "ADMIN"
        private const val MANAGER = "MANAGER"

        @Container
        val container = PostgreSQLContainer(POSTGRES_IMAGE).apply {
            withDatabaseName("test_db")
            withUsername("test_user")
            withPassword("test_password")
        }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }
    }
}