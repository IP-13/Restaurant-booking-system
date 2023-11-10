package com.ip13.main.service

import com.ip13.main.model.entity.BlackList
import com.ip13.main.repository.BlackListRepository
import com.ip13.main.security.service.UserService
import org.springframework.stereotype.Service

@Service
class BlackListService(
    private val blackListRepository: BlackListRepository,
    private val userService: UserService,
) {
    fun save(blackList: BlackList): Int {
        // check if user exists
        userService.findByIdOrThrow(blackList.user.id)
        return blackListRepository.save(blackList).id
    }
}