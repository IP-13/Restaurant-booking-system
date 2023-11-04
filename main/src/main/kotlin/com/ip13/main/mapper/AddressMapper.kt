package com.ip13.main.mapper

import com.ip13.main.model.dto.AddressDto
import com.ip13.main.model.entity.Address

object AddressMapper {
    fun fromAddressDto(dto: AddressDto): Address {
        return Address(
            country = dto.country,
            city = dto.city,
            street = dto.street,
            building = dto.building,
            entrance = dto.entrance,
            floor = dto.floor,
        )
    }
}