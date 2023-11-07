package com.ip13.main.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AdminRepository : CrudRepository<Admin, Int> {
    @Query("select * from admin where user_id = :user_id and is_active = true", nativeQuery = true)
    fun getActiveByUserIdOrNull(
        @Param("user_id")
        userId: Int
    ): Admin?
}