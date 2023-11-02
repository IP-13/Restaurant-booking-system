package com.ip13.main.exceptionHandling

import com.ip13.main.exceptionHandling.dto.CommonResponse
import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class CommonControllerAdvice {
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<CommonResponse> {
        val message = "${LocalDateTime.now()}: $ex.message"
        return ResponseEntity(CommonResponse(message), HttpStatus.BAD_REQUEST)
    }
}