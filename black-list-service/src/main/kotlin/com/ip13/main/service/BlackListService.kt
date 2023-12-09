package com.ip13.main.service

import com.ip13.main.BlackListRepository
import com.ip13.main.model.dto.BlackListRequest
import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.toBlackList
import org.springframework.stereotype.Service

@Service
class BlackListService(
    private val blackListRepository: BlackListRepository,
) {
    fun findByUserId(userId: Int): List<BlackList> {
        return blackListRepository.findByUserId(userId)
    }

    fun findAll(): Iterable<BlackList> {
        return blackListRepository.findAll()
    }

    fun save(blackListRequest: BlackListRequest): BlackList {
        return blackListRepository.save(blackListRequest.toBlackList())
    }
}