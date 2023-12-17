package com.ip13.main.model.entity

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class FileInfoId(
    val bucketName: String = "",
    val objectName: String = "",
) : Serializable