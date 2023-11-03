package com.ip13.main.service

import com.ip13.main.provider.EntitiesProvider
import com.ip13.main.repository.TableReserveTicketRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TableReserveServiceTest {
    @MockK
    private lateinit var tableReserveTicketRepository: TableReserveTicketRepository

    @InjectMockKs
    private lateinit var tableReserveService: TableReserveService

    @Test
    fun saveTest() {
        val tableReserveTicket = EntitiesProvider.getDefaultTableReserveTicket(id = 13)

        every { tableReserveTicketRepository.save(any()) } returns tableReserveTicket

        Assertions.assertThat(tableReserveService.save(tableReserveTicket)).isEqualTo(13)
    }
}
