package com.ip13.main.exceptionHandling

import com.ip13.main.exceptionHandling.dto.CommonResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class CommonControllerAdvice {
    @ExceptionHandler
    fun handleException(e: Exception): ResponseEntity<CommonResponse> {
        val message = e.message ?: "${LocalDateTime.now()}: exception occurred"
        return ResponseEntity(CommonResponse(message), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}