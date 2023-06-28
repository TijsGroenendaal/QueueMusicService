package nl.tijsgroenendaal.spotifyfacade.exceptions

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.ErrorCode
import nl.tijsgroenendaal.qumu.exceptions.FallbackErrorCodes

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import kotlin.Exception

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(value = [BadRequestException::class])
    protected fun handleBadRequestException(exception: BadRequestException): ResponseEntity<ErrorResponse> {
        log(exception)
        return ResponseEntity.status(HttpStatusCode.valueOf(exception.status)).body(ErrorResponse(exception))
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleGenericException(exception: Exception): ResponseEntity<ErrorResponse> {
        log(exception)
        return ResponseEntity.internalServerError().body(ErrorResponse(FallbackErrorCodes.INTERNAL_SERVER_ERROR))
    }

    data class ErrorResponse(
        val code: String,
        val status: Int,
        val message: String
    ) {
        constructor(error: ErrorCode): this(error.getCode(), error.getStatus(), error.getMessage())
        constructor(exception: BadRequestException): this(exception.code, exception.status, exception.message)
    }

    private fun log(exception: Exception) {
        exception.printStackTrace()
    }
}