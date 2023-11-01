package com.ip13.main.service

import com.ip13.main.model.entity.Address
import com.ip13.main.repository.AddressRepository
import org.springframework.stereotype.Service

@Service
class AddressService(
    val addressRepository: AddressRepository,
) {
    fun save(address: Address): Int {
        return addressRepository.save(address).id
    }
}