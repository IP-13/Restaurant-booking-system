package com.ip13.main.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository: CrudRepository<Address, Int>