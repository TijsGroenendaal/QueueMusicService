package nl.tijsgroenendaal.qumusecurity.security.helper

import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicAuthentication
import nl.tijsgroenendaal.qumu.exceptions.InvalidJwtSubjectException

import io.jsonwebtoken.Claims

import org.springframework.security.core.context.SecurityContextHolder

import java.util.UUID

fun getAuthenticationContext(): QueueMusicAuthentication =
    SecurityContextHolder.getContext().authentication as QueueMusicAuthentication


fun getAuthenticationContextSubject(): UUID =
    getAuthenticationContext().principal.id


fun Claims.getUserIdFromSubject(): UUID {
    return getUserIdFromSubject(this.subject)
}

@Throws(InvalidJwtSubjectException::class)
fun getUserIdFromSubject(subject: String): UUID {
    return try {
        UUID.fromString(subject)
    } catch (e: IllegalArgumentException) {
        throw InvalidJwtSubjectException(subject)
    }
}

fun Claims.getDeviceIdFromClaims(): String? {
    return this["deviceId"] as String?
}