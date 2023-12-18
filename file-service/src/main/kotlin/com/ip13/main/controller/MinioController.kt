package com.ip13.main.controller

import com.ip13.main.model.dto.DownloadRequest
import com.ip13.main.service.MinioService
import com.ip13.main.util.getLogger
import jakarta.validation.constraints.NotNull
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Validated
@RestController
@RequestMapping("/file")
class MinioController(
    private val minioService: MinioService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/upload/{bucket}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        principal: Principal,
        @RequestPart
        @NotNull
        multipartFile: MultipartFile,
        @PathVariable
        @NotNull
        bucket: String,
    ) {
        log.debug("/file/upload/{} endpoint invoked", bucket)

        minioService.upload(multipartFile, bucket, principal.name)
    }

    @PostMapping("/download")
    fun download(
        principal: Principal,
        @RequestBody
        downloadRequest: DownloadRequest,
    ) {
        log.debug("/file/download endpoint invoked")

        minioService.download(downloadRequest, principal.name)
    }

    @DeleteMapping("/delete")
    fun delete(
        principal: Principal,
        @RequestParam
        bucket: String,
        @RequestParam
        filename: String,
    ) {
        log.debug("/file/delete endpoint invoked")

        minioService.delete(bucketName = bucket, objectName = filename, currUsername = principal.name)
    }
}