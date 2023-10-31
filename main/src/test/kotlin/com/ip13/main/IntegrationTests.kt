package com.ip13.main

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit4.SpringRunner
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
class IntegrationTests(
    @Autowired val jdbc: JdbcTemplate,
) {
    @AfterEach
    fun cleanUp() {
        // TODO()
    }

    companion object {
        private const val POSTGRES_IMAGE = "postgres:16.0"

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

    @Test
    fun `container is running test`() {
        println(container.databaseName)
    }

    @Test
    fun `test successful insert and select with address table`() {
        jdbc.update(
            "insert into address(country, city, street, building, entrance, floor) " +
                    "values('Russia', 'Saint-Petersburg', 'Lenina', 12, 1, 3)"
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
}