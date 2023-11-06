package com.ip13.main.repository

import com.ip13.main.model.entity.BookingConstraint
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BookingConstraintRepository : CrudRepository<BookingConstraint, Int> {
    @Query(
        "select count(*) from booking_constraint where restaurant_id = :restaurant_id and " +
                "((:from_date < till_date and :from_date >= from_date) or " +
                "(:till_date <= till_date and :till_date > from_date))",
        nativeQuery = true
    )
    fun isOpen(
        @Param("from_date")
        fromDate: LocalDateTime,
        @Param("till_date")
        tillDate: LocalDateTime,
        @Param("restaurant_id")
        restaurantId: Int,
    ): Int
}