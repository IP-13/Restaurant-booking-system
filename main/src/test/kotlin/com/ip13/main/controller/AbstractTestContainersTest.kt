package com.ip13.main.controller

import com.ip13.main.security.repository.UserRepository
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.PostgreSQLContainer
import java.io.File

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["security.enabled=true"],
)
@AutoConfigureMockMvc
abstract class AbstractTestContainersTest {
    @Autowired
    lateinit var jdbc: JdbcTemplate

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun cleanUp() {
        jdbc.execute("truncate table black_list cascade")
        jdbc.execute("truncate table booking_constraint cascade")
        jdbc.execute("truncate table grade_manager cascade")
        jdbc.execute("truncate table grade_visitor cascade")
        jdbc.execute("truncate table table_reserve_ticket cascade")
        jdbc.execute("truncate table restaurant cascade")
        jdbc.execute("truncate table restaurant_add_ticket cascade")
        jdbc.execute("delete from user_t where id != 100")
    }

    fun loadAsString(filePath: String): String {
        return File("src/test/resources/$filePath").readText()
    }

    fun registerDefaultUser() {
        val body = loadAsString("json/default_user_register_dto.json")

        mockMvc.post("/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
            with(
                SecurityMockMvcRequestPostProcessors.user("ip13")
                    .password("13579").authorities(SimpleGrantedAuthority(ADMIN))
            )
        }.andExpect {
            MockMvcResultMatchers.status().`is`(200)
            content {
                // проверка что приходит токен
                jsonPath(
                    "token",
                    CoreMatchers.containsString(""),
                )
            }
        }
    }

    companion object {
        private const val POSTGRES_IMAGE = "postgres:16.0"
        const val ADMIN = "ADMIN"
        const val MANAGER = "MANAGER"

        private val container = PostgreSQLContainer(POSTGRES_IMAGE).apply {
            withDatabaseName("test_db")
            withUsername("test_user")
            withPassword("test_password")
            start()
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