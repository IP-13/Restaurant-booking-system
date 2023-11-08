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
class ManagerServiceTest {
    @MockK
    private lateinit var managerRepository: ManagerRepository

    @InjectMockKs
    private lateinit var managerService: ManagerService

    @Test
    fun saveTest() {
        val manager = EntitiesProvider.getDefaultManager(id = 13)

        every { managerRepository.save(any()) } returns manager

        Assertions.assertThat(managerService.save(manager)).isEqualTo(13)
    }
}