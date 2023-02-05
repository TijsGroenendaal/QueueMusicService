package nl.tijsgroenendaal.queuemusicservice.helper

import nl.tijsgroenendaal.queuemusicservice.security.QueueMusicAuthentication

import org.springframework.security.core.context.SecurityContextHolder

import java.util.UUID

fun getAuthenticationContext(): QueueMusicAuthentication =
    SecurityContextHolder.getContext().authentication as QueueMusicAuthentication


fun getAuthenticationContextSubject(): UUID =
    getAuthenticationContext().principal.qmId
