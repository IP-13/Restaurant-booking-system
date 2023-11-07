package com.ip13.main.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ManagerRepository : CrudRepository<Manager, Int> {
    @Query(
        "select (select count(*) from manager where id = :manager_id and restaurant_id = :restaurant_id" +
                " and is_active = true) = 1",
        nativeQuery = true
    )
    fun checkIfWorksInRestaurantById(
        @Param("manager_id")
        managerId: Int,
        @Param("restaurant_id")
        restaurantId: Int,
    ): Boolean

    @Query("select is_active from manager where id = :manager_id", nativeQuery = true)
    fun checkIfActive(
        @Param("manager_id")
        managerId: Int,
    ): Boolean

    @Query("select * from manager where user_id = :user_id", nativeQuery = true)
    fun getManagerByUserId(
        @Param("user_id")
        userId: Int,
    ): Manager?
}