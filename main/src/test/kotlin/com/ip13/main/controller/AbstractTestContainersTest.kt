package com.ip13.main.controller

import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.PostgreSQLContainer
import java.io.File

/**
 * NOTE: test_db doesn't contain mega_admin
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["security.enabled=true"],
)
@AutoConfigureMockMvc
@ActiveProfiles("tc-flyway")
abstract class AbstractTestContainersTest {
    @Autowired
    lateinit var jdbc: JdbcTemplate

    @Autowired
    lateinit var mockMvc: MockMvc

    /**
     * Truncates all tables and restores values of all id-sequences to 100. It's necessary for tests, because
     * a bunch of methods take entity id as a parameter. Another way to do it for tests - always check currval
     * of sequence, but it that case one cannot user static json for requests.
     */
    @BeforeEach
    fun cleanUp() {
        jdbc.execute("truncate table black_list cascade")
        jdbc.execute("truncate table booking_constraint cascade")
        jdbc.execute("truncate table visitor_grade cascade")
        jdbc.execute("truncate table restaurant_grade cascade")
        jdbc.execute("truncate table table_reserve_ticket cascade")
        jdbc.execute("truncate table restaurant cascade")
        jdbc.execute("truncate table restaurant_add_ticket cascade")
        jdbc.execute("truncate table user_t cascade")
        jdbc.execute("select setval('black_list_id_seq', 100, false)")
        jdbc.execute("select setval('booking_constraint_id_seq', 100, false)")
        jdbc.execute("select setval('visitor_grade_id_seq', 100, false)")
        jdbc.execute("select setval('restaurant_grade_id_seq', 100, false)")
        jdbc.execute("select setval('table_reserve_ticket_id_seq', 100, false)")
        jdbc.execute("select setval('restaurant_id_seq', 100, false)")
        jdbc.execute("select setval('restaurant_add_ticket_id_seq', 100, false)")
        jdbc.execute("select setval('user_t_id_seq', 100, false)")
    }

    fun loadAsString(filePath: String): String {
        return File("src/test/resources/$filePath").readText()
    }

    fun registerDefaultUser() {
        val body = loadAsString("json/default_user_register.json")

        mockMvc.post("/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
            with(
                SecurityMockMvcRequestPostProcessors.user("ip13").authorities(SimpleGrantedAuthority(ADMIN))
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