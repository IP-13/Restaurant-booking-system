package com.ip13.main.service

import com.ip13.main.model.dto.request.BlackListRequest
import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.toBlackList
import com.ip13.main.repository.BlackListRepository
import com.ip13.main.security.service.UserService
import org.springframework.stereotype.Service

@Service
class BlackListService(
    private val blackListRepository: BlackListRepository,
    private val userService: UserService,
) {
    fun processRequest(request: BlackListRequest): Int {
        // check if user exists
        val blackList = request.toBlackList()
        userService.findByIdOrThrow(blackList.user.id)
        return blackListRepository.save(blackList).id
    }

    fun save(blackList: BlackList): Int {
        userService.findByIdOrThrow(blackList.user.id)
        return blackListRepository.save(blackList).id
    }
}