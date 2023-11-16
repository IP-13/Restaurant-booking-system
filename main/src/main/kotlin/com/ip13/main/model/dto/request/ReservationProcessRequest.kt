package com.ip13.main.model.dto.request

import com.ip13.main.model.enums.TableReserveStatus
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class ReservationProcessRequest(
    @Positive
    val tableReserveTicketId: Int,
    @Size(max = 25, message = "Manager comment length should be less than 25 symbols")
    val managerComment: String?,
    @NotNull
    val status: TableReserveStatus,
)