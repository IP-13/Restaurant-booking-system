package com.ip13.main.model.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class BookingConstraintTest {
    @Test
    fun overridenMethodsTest() {
        val restaurant = Restaurant()
        val fromDate = LocalDateTime.now()
        val tillDate = LocalDateTime.now()

        val bookingConstraint1 = BookingConstraint(
            fromDate = fromDate,
            tillDate = tillDate,
            restaurant = restaurant,
        )

        val bookingConstraint2 = BookingConstraint(
            fromDate = fromDate,
            tillDate = tillDate,
            restaurant = restaurant,
        )

        assertThat(bookingConstraint1).isEqualTo(bookingConstraint2)
        assertThat(bookingConstraint1.hashCode()).isEqualTo(bookingConstraint2.hashCode())
        assertThat(bookingConstraint1.toString()).isEqualTo(bookingConstraint2.toString())
    }
}