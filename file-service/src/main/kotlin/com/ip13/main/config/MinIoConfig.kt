package com.ip13.main.config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinIoConfig {
    @Value("\${minio.url}")
    private lateinit var url: String

    @Value("\${minio.accessKey}")
    private lateinit var accessKey: String

    @Value("\${minio.secretKey}")
    private lateinit var secretKey: String

    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build()
    }
}