package nl.tijsgroenendaal.queuemusicservice.helper

import nl.tijsgroenendaal.queuemusicservice.exceptions.InvalidJwtSubjectException
import nl.tijsgroenendaal.queuemusicservice.security.model.QueueMusicAuthentication

import io.jsonwebtoken.Claims

import org.springframework.security.core.context.SecurityContextHolder

import java.util.UUID

fun getAuthenticationContext(): QueueMusicAuthentication =
    SecurityContextHolder.getContext().authentication as QueueMusicAuthentication


fun getAuthenticationContextSubject(): UUID =
    getAuthenticationContext().principal.qmId


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