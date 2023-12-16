package com.ip13.main.service

import com.ip13.main.model.dto.UploadFileRequest
import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.stereotype.Service

@Service
class MinioService(
    private val minioClient: MinioClient,
) {
    fun upload(request: UploadFileRequest) {
        val bucket = request.bucket

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
        }

        val multipartFile = request.multipartFile

        val filename = multipartFile.name

        val stream = multipartFile.inputStream

        minioClient.putObject(
            PutObjectArgs.builder()
                .stream(stream, stream.available().toLong(), -1)
                .bucket(bucket)
                .`object`(filename)
                .build()
        )
    }
}