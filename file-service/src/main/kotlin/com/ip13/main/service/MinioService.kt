package com.ip13.main.service

import com.ip13.main.repository.FileInfoRepository
import com.ip13.main.model.dto.DownloadRequest
import com.ip13.main.model.entity.FileInfo
import com.ip13.main.model.entity.FileInfoId
import com.ip13.main.util.getLogger
import io.minio.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Service
class MinioService(
    private val minioClient: MinioClient,
    private val fileInfoRepository: FileInfoRepository,
) {
    private val log = getLogger(javaClass)

    fun upload(multipartFile: MultipartFile, bucketName: String, currUsername: String) {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            log.debug("creating new bucket: {}", bucketName)
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
        }

        val objectName = multipartFile.originalFilename ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Null filename"
        )

        val stream = multipartFile.inputStream

        fileInfoRepository.save(
            FileInfo(
                fileInfoId = FileInfoId(bucketName = bucketName, objectName = objectName),
                username = currUsername,
            )
        )

        minioClient.putObject(
            PutObjectArgs.builder()
                .stream(stream, stream.available().toLong(), -1)
                .bucket(bucketName)
                .`object`(objectName)
                .build()
        )
    }

    fun download(request: DownloadRequest, currUsername: String) {
        checkIfFileExistsAndFileOwner(
            bucketName = request.bucketName,
            objectName = request.objectName,
            currUsername = currUsername
        )

        minioClient.downloadObject(
            DownloadObjectArgs.builder()
                .bucket(request.bucketName)
                .`object`(request.objectName)
                .filename(request.filename)
                .build()
        )
    }

    fun delete(bucketName: String, objectName: String, currUsername: String) {
        checkIfFileExistsAndFileOwner(
            bucketName = bucketName,
            objectName = objectName,
            currUsername = currUsername
        )

        fileInfoRepository.deleteById(FileInfoId(bucketName = bucketName, objectName = objectName))

        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .build()
        )
    }

    private fun checkIfFileExistsAndFileOwner(bucketName: String, objectName: String, currUsername: String) {
        val fileOwner = fileInfoRepository.findByIdOrNull(
            FileInfoId(
                bucketName = bucketName,
                objectName = objectName
            )
        )?.username ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "No file with name $objectName in bucket $bucketName"
        )

        if (fileOwner != currUsername) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "It's not your file")
        }
    }
}