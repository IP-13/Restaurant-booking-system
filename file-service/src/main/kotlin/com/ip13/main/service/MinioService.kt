package com.ip13.main.service

import com.ip13.main.util.getLogger
import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MinioService(
    private val minioClient: MinioClient,
) {
    private val log = getLogger(javaClass)

    fun upload(multipartFile: MultipartFile, bucket: String) {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            log.debug("creating new bucket: {}", bucket)
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
        }

        val filename = multipartFile.originalFilename
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