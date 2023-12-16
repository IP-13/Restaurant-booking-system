package com.ip13.main.exceptionHandling

import com.ip13.main.exceptionHandling.dto.CommonResponse
import com.ip13.main.exceptionHandling.exception.NotFoundException
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class CommonControllerAdvice {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<CommonResponse> {
        val message = "${LocalDateTime.now()}: $ex.message"
        return ResponseEntity(CommonResponse(message), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(ex: ExpiredJwtException): ResponseEntity<CommonResponse> {
        return ResponseEntity(CommonResponse(message = "Jwt token has expired. $ex.message"), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<CommonResponse> {
        val message = "Looks like you entered invalid data.\n${ex.message}"
        return ResponseEntity(CommonResponse(message), HttpStatus.BAD_REQUEST)
    }
}