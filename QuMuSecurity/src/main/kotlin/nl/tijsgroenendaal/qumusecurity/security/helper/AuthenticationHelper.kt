package nl.tijsgroenendaal.qumusecurity.security.helper

import nl.tijsgroenendaal.qumusecurity.security.exceptions.InvalidUserIdException
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicAuthentication

import org.springframework.security.core.context.SecurityContextHolder

import java.util.UUID

fun getAuthenticationContext(): QueueMusicAuthentication = SecurityContextHolder.getContext().authentication as QueueMusicAuthentication

fun getAuthenticationContextSubject(): UUID = getAuthenticationContext().claims.getUserId()

fun getUserIdFromClaim(claim: String): UUID {
    return try {
        UUID.fromString(claim)
    } catch (e: IllegalArgumentException) {
        throw InvalidUserIdException("UserId is not a uuid")
    }
}