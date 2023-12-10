package com.ip13.main.model

import com.ip13.main.model.dto.BlackListRequest
import com.ip13.main.model.entity.BlackList

fun BlackListRequest.toBlackList(): BlackList {
    return BlackList(
        username = this.username,
        fromDate = this.fromDate,
        tillDate = this.tillDate,
        reason = this.reason,
    )
}