package com.ip13.main.service

import com.ip13.main.model.entity.Address
import com.ip13.main.repository.AddressRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
class AddressServiceTest {
    @MockK
    private lateinit var addressRepository: AddressRepository

    @InjectMockKs
    private lateinit var addressService: AddressService

    @Test
    fun saveTest() {
        every { addressRepository.save(any()) } returns Address(id = 13)
        Assertions.assertThat(addressService.save(Address())).isEqualTo(13)
    }
}