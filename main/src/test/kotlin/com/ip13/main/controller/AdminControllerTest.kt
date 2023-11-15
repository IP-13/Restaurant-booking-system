package com.ip13.main.controller

import com.ip13.main.security.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
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
class AdminControllerTest(
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
        jdbc.execute("delete from user_t where id != 100")
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