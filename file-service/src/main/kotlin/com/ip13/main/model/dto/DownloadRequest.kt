package com.ip13.main.model.dto

data class DownloadRequest(
    val bucketName: String,
    // name of file in minio
    val objectName: String,
    // name which will be assigned to downloaded file
    val filename: String,
)
