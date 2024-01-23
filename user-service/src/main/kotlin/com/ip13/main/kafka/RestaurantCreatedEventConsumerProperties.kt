package com.ip13.main.kafka

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
@ConfigurationProperties(prefix = "kafka")
class RestaurantCreatedEventConsumerProperties {
    lateinit var consumer: Map<String, KafkaProperties.Consumer>

    @Bean
    fun restaurantCreatedEventConsumer(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, String> = ConcurrentKafkaListenerContainerFactory()
        factory.consumerFactory = consumerFactory("restaurant-created-event-consumer")
        return factory
    }

    private fun consumerFactory(consumerName: String): ConsumerFactory<String, Any> {
        val properties = consumer[consumerName]?.buildProperties()
            ?: throw RuntimeException("No properties found for consumer $consumerName")
        return DefaultKafkaConsumerFactory(properties)
    }
}