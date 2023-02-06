package nl.tijsgroenendaal.queuemusicservice.security

import java.util.UUID

data class QueueMusicPrincipalAuthentication(
    val subscriptions: List<Authorities>,
    val qmId: UUID
)