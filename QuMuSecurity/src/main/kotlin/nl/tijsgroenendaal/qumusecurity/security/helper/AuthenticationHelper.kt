package nl.tijsgroenendaal.qumusecurity.security.helper

import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicAuthentication

import io.jsonwebtoken.Claims
import nl.tijsgroenendaal.qumu.exceptions.AuthErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException

import org.springframework.security.core.context.SecurityContextHolder

import java.util.UUID

fun getAuthenticationContext(): QueueMusicAuthentication =
    SecurityContextHolder.getContext().authentication as QueueMusicAuthentication


fun getAuthenticationContextSubject(): UUID =
    getAuthenticationContext().principal.id


fun Claims.getUserIdFromSubject(): UUID {
    return getUserIdFromSubject(this.subject)
}

fun getUserIdFromSubject(subject: String): UUID {
    return try {
        UUID.fromString(subject)
    } catch (e: IllegalArgumentException) {
        throw BadRequestException(AuthErrorCodes.INVALID_JWT_SUBJECT)
    }
}

fun Claims.getDeviceIdFromClaims(): String? {
    return this["deviceId"] as String?
}