package com.ip13.main.service

import com.ip13.main.repository.BlackListRepository
import com.ip13.main.model.dto.BlackListRequest
import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.toBlackList
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class BlackListService(
    private val blackListRepository: BlackListRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    fun findByUsername(username: String): List<BlackList> {
        return blackListRepository.findByUsername(username)
    }

    fun findAll(): Iterable<BlackList> {
        return blackListRepository.findAll()
    }

    fun save(blackListRequest: BlackListRequest, managerName: String): BlackList {
        val message = "$managerName: You've been banned from our restaurant till ${blackListRequest.tillDate}"

        kafkaTemplate.send(topic, message)

        return blackListRepository.save(blackListRequest.toBlackList())
    }

    companion object {
        private const val topic = "bad people"
    }
}