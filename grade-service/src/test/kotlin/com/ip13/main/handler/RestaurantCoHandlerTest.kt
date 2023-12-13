package com.ip13.main.handler

import com.ip13.main.model.entity.Restaurant
import com.ip13.main.repository.RestaurantCoRepository
import com.ip13.main.sql.RunSql
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class RestaurantCoHandlerTest : AbstractTestContainers() {
    @Autowired
    private lateinit var restaurantCoRepository: RestaurantCoRepository

    @Test
    @RunSql(["/sql/create_new_tables.sql"])
    fun runSql() {
        runBlocking {
            restaurantCoRepository.save(Restaurant(10, 10, 10))
            println(restaurantCoRepository.findAll().toList().size)
        }
    }
}