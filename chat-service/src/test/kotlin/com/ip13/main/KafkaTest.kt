package com.ip13.main

import com.ip13.main.event.BlackListNotificationEvent
import com.ip13.main.webSocket.Handler
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.concurrent.TimeUnit

@SpringBootTest
@TestPropertySource(
    properties = [
        "spring.kafka.consumer.auto-offset-reset=earliest",
    ]
)
@ActiveProfiles("kafka-test")
class KafkaTest {
    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, BlackListNotificationEvent>

    @Autowired
    private lateinit var handler: Handler

    @Test
    fun test() {
        kafkaTemplate.send(
            BAD_PEOPLE_TOPIC, BlackListNotificationEvent(
                senderName = "me",
                receiverName = "someone",
                message = "hello",
            )
        )

        Awaitility.await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(10, TimeUnit.SECONDS)
            .untilAsserted {
                assertAll(
                    { assertThat(handler.undeliveredMessages.size).isEqualTo(1) },
                    { assertThat(handler.undeliveredMessages["someone"]).isEqualTo(mutableListOf("me: hello")) },
                )
            }
    }

    companion object {
        private const val POSTGRES_IMAGE = "postgres:16.0"
        private const val KAFKA_IMAGE = "confluentinc/cp-kafka"
        const val ADMIN = "ADMIN"
        const val MANAGER = "MANAGER"

        private const val BAD_PEOPLE_TOPIC = "bad-people"

        private val kafkaContainer = KafkaContainer(DockerImageName.parse(KAFKA_IMAGE))

        private val postgreSQLContainer = PostgreSQLContainer(POSTGRES_IMAGE).apply {
            withDatabaseName("test_db")
            withUsername("test_user")
            withPassword("test_password")
        }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
            registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers)
        }

        @JvmStatic
        @BeforeAll
        internal fun setUp() {
            postgreSQLContainer.start()
            kafkaContainer.start()
        }
    }
}