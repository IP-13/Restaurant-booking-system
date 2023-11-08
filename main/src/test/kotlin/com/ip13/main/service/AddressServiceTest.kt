package com.ip13.main.service

import com.ip13.main.provider.EntitiesProvider
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
        val address = EntitiesProvider.getDefaultAddress(id = 13)

        every { addressRepository.save(any()) } returns address

        Assertions.assertThat(addressService.save(Address())).isEqualTo(13)
    }
}