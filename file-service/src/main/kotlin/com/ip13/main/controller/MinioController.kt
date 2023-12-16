package com.ip13.main.controller

import com.ip13.main.service.MinioService
import com.ip13.main.util.getLogger
import jakarta.validation.constraints.NotNull
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Validated
@RestController
@RequestMapping("/file")
class MinioController(
    private val minioService: MinioService,
) {
    private val log = getLogger(javaClass)

    @PostMapping("/upload/{bucket}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestPart
        @NotNull
        multipartFile: MultipartFile,
        @PathVariable
        @NotNull
        bucket: String,
    ) {
        log.debug("/file/upload/{} endpoint invoked", bucket)

        minioService.upload(multipartFile, bucket)
    }

    @GetMapping("/download")
    fun download(
        @RequestParam
        bucket: String,
        @RequestParam
        objectName: String,
        @RequestParam
        filename: String,
    ) {
        log.debug("/file/download endpoint invoked")

        minioService.download(bucket = bucket, objectName = objectName, filename = filename)
    }

    @DeleteMapping("/delete")
    fun delete(
        @RequestParam
        bucket: String,
        @RequestParam
        filename: String,
    ) {
        log.debug("/file/delete endpoint invoked")

        minioService.delete(bucket = bucket, filename = filename)
    }
}