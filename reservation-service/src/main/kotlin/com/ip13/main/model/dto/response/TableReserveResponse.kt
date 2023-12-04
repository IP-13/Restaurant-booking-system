package com.ip13.main.model.dto.response

import com.ip13.main.model.enums.TableReserveStatus

data class TableReserveResponse(
    val status: TableReserveStatus,
)