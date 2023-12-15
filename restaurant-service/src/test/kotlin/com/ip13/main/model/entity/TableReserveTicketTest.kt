package com.ip13.main.model.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TableReserveTicketTest {
    @Test
    fun overridenMethodsTest() {
        val creationDate = LocalDateTime.now()
        val fromDate = LocalDateTime.now()
        val tillDate = LocalDateTime.now()
        val restaurant = Restaurant()

        val tableReserveTicket1 = TableReserveTicket(
            restaurant = restaurant,
            creationDate = creationDate,
            fromDate = fromDate,
            tillDate = tillDate
        )
        val tableReserveTicket2 = TableReserveTicket(
            restaurant = restaurant,
            creationDate = creationDate,
            fromDate = fromDate,
            tillDate = tillDate
        )

        Assertions.assertThat(tableReserveTicket1).isEqualTo(tableReserveTicket2)
        Assertions.assertThat(tableReserveTicket1.hashCode()).isEqualTo(tableReserveTicket2.hashCode())
        Assertions.assertThat(tableReserveTicket1.toString()).isEqualTo(tableReserveTicket2.toString())
    }
}