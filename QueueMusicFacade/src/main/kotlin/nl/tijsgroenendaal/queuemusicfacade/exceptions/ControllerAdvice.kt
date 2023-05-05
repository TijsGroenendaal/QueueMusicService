package nl.tijsgroenendaal.queuemusicfacade.exceptions

import nl.tijsgroenendaal.qumu.exceptions.AccessTokenExpiredException
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.InvalidJwtException
import nl.tijsgroenendaal.qumu.exceptions.UnAuthorizedException

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import kotlin.Exception

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(value = [BadRequestException::class])
    protected fun handleBadRequestException(exception: BadRequestException): ResponseEntity<ErrorResponse> {
        log(exception)
        return ResponseEntity.badRequest().body(ErrorResponse(
            HttpStatus.BAD_REQUEST.value().toString(),
            exception.code,
            exception.message
        ))
    }

    @ExceptionHandler(value = [InvalidJwtException::class])
    protected fun handleUnauthenticatedException(exception: Exception): ResponseEntity<ErrorResponse> {
        log(exception)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(
            HttpStatus.UNAUTHORIZED.value().toString(),
            "Unauthenticated",
            "Invalid Jwt Token"
        ))
    }

    @ExceptionHandler(value = [UnAuthorizedException::class])
    protected fun handleUnauthorizedException(exception: UnAuthorizedException): ResponseEntity<ErrorResponse> {
        log(exception)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse(
            HttpStatus.FORBIDDEN.value().toString(),
            "Unauthorized",
            "No Authorized For Action"
        ))
    }

    @ExceptionHandler(value = [AccessTokenExpiredException::class])
    protected fun handleUserLinkAccessTokenExpiredException(exception: AccessTokenExpiredException): ResponseEntity<ErrorResponse> {
        log(exception)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse(
            HttpStatus.FORBIDDEN.value().toString(),
            "Unauthorized",
            "User Link Access Token Expired"
        ))
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleGenericException(exception: Exception): ResponseEntity<ErrorResponse> {
        log(exception)
        return ResponseEntity.badRequest().body(ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value().toString(),
            "Internal Server Error",
            "Something went wrong"
        ))
    }

    data class ErrorResponse(
        val status: String,
        val code: String,
        val description: String?
    )

    private fun log(exception: Exception) {
        exception.printStackTrace()
    }

}