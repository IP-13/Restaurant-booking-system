package com.ip13.main.kafka

import com.ip13.main.event.RegistrationEvent
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
@ConfigurationProperties(prefix = "kafka.producer")
class RegistrationEventProducerProperties {
    private lateinit var producerMap: Map<String, KafkaProperties.Producer>

    @Bean
    fun registrationEventProducer(): KafkaTemplate<String, RegistrationEvent> {
        return KafkaTemplate(producerFactory("registration-event-producer"))
    }

    private fun producerFactory(producerName: String): ProducerFactory<String, RegistrationEvent> {
        val properties = producerMap[producerName]?.buildProperties()
            ?: throw RuntimeException("No properties found for producer $producerName")
        return DefaultKafkaProducerFactory(properties)
    }
}