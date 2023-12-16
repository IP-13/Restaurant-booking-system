package com.ip13.main.controller

import com.ip13.main.service.MinioService
import jakarta.validation.constraints.NotNull
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Validated
@RestController
@RequestMapping("/file")
class MinioController(
    private val minioService: MinioService,
) {
    @PostMapping("/upload/{bucket}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestPart
        @NotNull
        multipartFile: MultipartFile,
        @PathVariable
        @NotNull
        bucket: String,
    ) {
        minioService.upload(multipartFile, bucket)
    }
}