package nl.tijsgroenendaal.qumusecurity.security.model

import java.util.UUID

data class QueueMusicPrincipalAuthentication(
    val id: UUID,
    val deviceId: String?,
    val authorities: Set<Authorities>,
)