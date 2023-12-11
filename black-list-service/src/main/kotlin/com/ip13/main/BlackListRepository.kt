package com.ip13.main

import com.ip13.main.model.entity.BlackList
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BlackListRepository : CrudRepository<BlackList, Int> {
    fun findByUsername(username: String): List<BlackList>
}