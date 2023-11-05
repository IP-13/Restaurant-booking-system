package com.ip13.main.model

import com.ip13.main.model.dto.AddressDto
import com.ip13.main.model.dto.BookingConstraintDto
import com.ip13.main.model.dto.TableReserveTicketDto
import com.ip13.main.provider.EntitiesProvider
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDateTime
import java.time.Month

class ExtensionsTest {
    @Test
    fun addressDtoToAddressTest() {
        val addressDto = AddressDto(
            country = "Secret",
            city = "C13",
            street = "first",
            building = 14,
            entrance = 6,
            floor = 4,
        )

        val address = addressDto.toAddress()

        assertAll(
            { Assertions.assertThat(address.country).isEqualTo("Secret") },
            { Assertions.assertThat(address.city).isEqualTo("C13") },
            { Assertions.assertThat(address.street).isEqualTo("first") },
            { Assertions.assertThat(address.building).isEqualTo(14) },
            { Assertions.assertThat(address.entrance).isEqualTo(6) },
            { Assertions.assertThat(address.floor).isEqualTo(4) },
        )
    }

    @Test
    fun restaurantAddTicketToRestaurant() {
        val restaurantAddTicket = EntitiesProvider.getDefaultRestaurantAddTicket(
            id = 9,
            name = "some name here",
            addressId = 588,
            description = "come up with a description on your own",
        )

        val restaurant = restaurantAddTicket.toRestaurant()

        assertAll(
            { Assertions.assertThat(restaurant.name).isEqualTo("some name here") },
            { Assertions.assertThat(restaurant.addressId).isEqualTo(588) },
            { Assertions.assertThat(restaurant.restaurantAddTicketId).isEqualTo(9) },
            { Assertions.assertThat(restaurant.description).isEqualTo("come up with a description on your own") },
        )
    }

    @Test
    fun tableReserveTicketDtoToTableReserveTicketTest() {
        val tableReserveTicketDto = TableReserveTicketDto(
            restaurantId = 41,
            fromDate = EntitiesProvider.getDefaultLocalDateTime(),
            tillDate = EntitiesProvider.getDefaultLocalDateTime().plusDays(4),
            numOfGuests = 4,
            userComment = "I'm hungry",
        )

        val tableReserveTicket = tableReserveTicketDto.toTableReserveTicket(22)

        assertAll(
            { Assertions.assertThat(tableReserveTicket.restaurantId).isEqualTo(41) },
            { Assertions.assertThat(tableReserveTicket.userId).isEqualTo(22) },
            {
                Assertions.assertThat(tableReserveTicket.fromDate).isEqualTo(
                    LocalDateTime.of(
                        2002,
                        Month.APRIL,
                        19,
                        13,
                        13,
                    )
                )
            },
            {
                Assertions.assertThat(tableReserveTicket.tillDate).isEqualTo(
                    LocalDateTime.of(
                        2002,
                        Month.APRIL,
                        23,
                        13,
                        13,
                    )
                )
            },
            { Assertions.assertThat(tableReserveTicket.numOfGuests).isEqualTo(4) },
            { Assertions.assertThat(tableReserveTicket.userComment).isEqualTo("I'm hungry") },
        )
    }

    @Test
    fun bookingConstraintDtoToBookingConstraintTest() {
        val bookingConstraintDto = BookingConstraintDto(
            restaurantId = 888,
            reason = "still",
            fromDate = EntitiesProvider.getDefaultLocalDateTime(),
            tillDate = EntitiesProvider.getDefaultLocalDateTime().plusDays(2),
        )

        val bookingConstraint = bookingConstraintDto.toBookingConstraint(1)

        assertAll(
            { Assertions.assertThat(bookingConstraint.restaurantId).isEqualTo(888) },
            { Assertions.assertThat(bookingConstraint.managerId).isEqualTo(1) },
            { Assertions.assertThat(bookingConstraint.reason).isEqualTo("still") },
            {
                Assertions.assertThat(bookingConstraint.fromDate).isEqualTo(
                    LocalDateTime.of(
                        2002,
                        Month.APRIL,
                        19,
                        13,
                        13,
                    )
                )
            },
            { Assertions.assertThat(bookingConstraint.tillDate).isEqualTo(
                LocalDateTime.of(
                    2002,
                    Month.APRIL,
                    21,
                    13,
                    13,
                )
            ) },
        )
    }
}