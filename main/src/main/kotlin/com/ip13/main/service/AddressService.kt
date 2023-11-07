package com.ip13.main.service

import com.ip13.main.repository.AddressRepository
import org.springframework.stereotype.Service

@Service
class AddressService(
    private val addressRepository: AddressRepository,
) {
    fun save(address: Address): Int {
        return addressRepository.save(address).id
    }
}