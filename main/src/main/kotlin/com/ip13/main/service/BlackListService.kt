package com.ip13.main.service

import com.ip13.main.model.entity.BlackList
import com.ip13.main.repository.BlackListRepository
import org.springframework.stereotype.Service

@Service
class BlackListService(
    private val blackListRepository: BlackListRepository,
) {
    fun save(blackList: BlackList): Int {
        return blackListRepository.save(blackList).id
    }
}