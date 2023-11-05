package com.ip13.main.model.dto

import com.ip13.main.model.entity.Address

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