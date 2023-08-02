package nl.tijsgroenendaal.qumu.exceptions

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
        return ResponseEntity.status(HttpStatusCode.valueOf(exception.status)).body(
            ErrorResponse(
            exception.code,
            exception.status,
            exception.message
        )
        )
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleGenericException(exception: Exception): ResponseEntity<ErrorResponse> {
        log(exception)
        return ResponseEntity.internalServerError().body(
            ErrorResponse(
            "Internal Server Error",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Something went wrong"
        )
        )
    }

    data class ErrorResponse(
        val code: String,
        val status: Int,
        val message: String
    )

    private fun log(exception: Exception) {
        exception.printStackTrace()
    }
}