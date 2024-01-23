package com.ip13.main.kafka

import com.ip13.main.event.RegistrationEvent
import com.ip13.main.handler.UserHandler
import com.ip13.main.model.entity.User
import org.springframework.kafka.annotation.KafkaListener

class RegistrationListener(
    private val userHandler: UserHandler,
) {

    @KafkaListener(
        topics = [REGISTRATION_TOPIC],
        groupId = "registration",
    )
    fun addUser(event: RegistrationEvent) {
        userHandler.save(
            User(
                username = event.username,
                numOfGrades = 0,
                sumOfGrades = 0,
            )
        )
    }

    companion object {
        private const val REGISTRATION_TOPIC = "registration"
    }
}