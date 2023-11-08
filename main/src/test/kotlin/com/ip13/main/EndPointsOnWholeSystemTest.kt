package com.ip13.main

import com.ip13.main.model.enums.Role
import com.ip13.main.security.repository.UserRepository
import com.ip13.main.utils.loadAsString
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["security.enabled=true"],
)
@ActiveProfiles("test-containers-flyway")
@Testcontainers
@AutoConfigureMockMvc
class EndPointsOnWholeSystemTest(
    @Autowired val jdbc: JdbcTemplate,
    @Autowired val userRepository: UserRepository,
    @Autowired val passwordEncoder: PasswordEncoder,
) {
    @BeforeEach
    fun cleanUp() {
        jdbc.execute("truncate table black_list cascade")
        jdbc.execute("truncate table booking_constraint cascade")
        jdbc.execute("truncate table grade_manager cascade")
        jdbc.execute("truncate table grade_visitor cascade")
        jdbc.execute("truncate table table_reserve_ticket cascade")
        jdbc.execute("truncate table restaurant cascade")
        jdbc.execute("truncate table restaurant_add_ticket cascade")
        jdbc.execute("truncate table user_t cascade")
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

    @Test
    fun `should return 400 status code when register with username that already exists`() {
        registerDefaultUser()

        val body = loadAsString("json/default_user_register_dto.json")

        mockMvc.post("/security/register") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status().`is`(400)
            content {
                jsonPath(
                    "message",
                    containsString("User with username ip13 already exists"),
                )
            }
        }

        val numOfUsersInDb = jdbc.queryForObject("select count(*) from user_t", Int::class.java)

        // новый юзер и mega_admin
        assertThat(numOfUsersInDb!!).isEqualTo(1)
    }

    @Test
    fun `should return token when login with valid name and password`() {
        registerDefaultUser()

        val body = loadAsString("json/default_user_register_dto.json")

        mockMvc.post("/security/login") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status().`is`(200)
            // assert that response contains token
            content {
                jsonPath(
                    "token",
                    containsString(""),
                )
            }
        }
    }

    @Test
    fun `should return 400 status code when wrong password`() {
        registerDefaultUser()

        val body = loadAsString("json/default_user_with_wrong_password.json")

        mockMvc.post("/security/login") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status().`is`(400)
            // assert that response contains token
            content {
                jsonPath(
                    "message",
                    containsString("passwords don't match"),
                )
            }
        }
    }

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

    private fun registerDefaultUser() {
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