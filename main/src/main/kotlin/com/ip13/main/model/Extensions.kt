package com.ip13.main.model

import com.ip13.main.model.dto.AddressDto
import com.ip13.main.model.dto.TableReserveTicketDto
import com.ip13.main.model.entity.Address
import com.ip13.main.model.entity.Restaurant
import com.ip13.main.model.entity.RestaurantAddTicket
import com.ip13.main.model.entity.TableReserveTicket

fun AddressDto.toAddress(): Address {
    return Address(
        country = this.country,
        city = this.city,
        street = this.street,
        building = this.building,
        entrance = this.entrance,
        floor = this.floor,
    )
}

fun RestaurantAddTicket.toRestaurant(): Restaurant {
    return Restaurant(
        name = this.name,
        addressId = this.addressId,
        restaurantAddTicketId = this.id,
        description = this.description,
    )
}

fun TableReserveTicketDto.toTableReserveTicket(userId: Int): TableReserveTicket {
    return TableReserveTicket(
        restaurantId = this.restaurantId,
        userId = userId,
        fromDate = this.fromDate,
        tillDate = this.tillDate,
        numOfGuests = this.numOfGuests,
        userComment = this.userComment,
    )
}