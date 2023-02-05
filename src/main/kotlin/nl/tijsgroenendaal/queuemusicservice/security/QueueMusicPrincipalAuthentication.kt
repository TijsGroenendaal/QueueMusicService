package nl.tijsgroenendaal.queuemusicservice.security

import java.util.UUID

data class QueueMusicPrincipalAuthentication(
    val subscriptions: Array<String>,
    val qmId: UUID
)