package com.ip13.main.model.dto.response

import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.entity.BookingConstraint
import com.ip13.main.model.enums.TableReserveStatus

data class TableReserveResponseDto(
    val blackListEntries: List<BlackList>,
    val bookingConstraints: List<BookingConstraint>,
    val status: TableReserveStatus,
    val comment: String? = null,
)