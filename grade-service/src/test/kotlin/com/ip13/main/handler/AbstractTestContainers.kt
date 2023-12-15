package com.ip13.main.handler

import com.ip13.main.sql.RunSqlExtension
import io.r2dbc.spi.ConnectionFactory
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import java.io.File

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(RunSqlExtension::class)
@AutoConfigureWebTestClient
abstract class AbstractTestContainers {
    @Autowired
    private lateinit var connectionFactory: ConnectionFactory

    @Autowired
    lateinit var webTestClient: WebTestClient

    fun loadAsString(filePath: String): String {
        return File("src/test/resources/$filePath").readText()
    }

    companion object {
        private const val POSTGRES_IMAGE = "postgres:16.0"
        const val ADMIN = "ADMIN"
        const val MANAGER = "MANAGER"

        private val container = PostgreSQLContainer(POSTGRES_IMAGE).apply {
            withDatabaseName("test_db")
            withUsername("test_user")
            withPassword("test_password")
        }

        private fun r2dbcUrl(): String {
            return "r2dbc:postgresql://${container.host}:${container.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)}/${container.databaseName}"
        }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url", Companion::r2dbcUrl)
            registry.add("spring.r2dbc.username", container::getUsername)
            registry.add("spring.r2dbc.password", container::getPassword)
        }

        @JvmStatic
        @BeforeAll
        internal fun setUp() {
            container.start()
        }
    }
}