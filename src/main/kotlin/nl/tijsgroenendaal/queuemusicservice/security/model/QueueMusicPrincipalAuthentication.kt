package nl.tijsgroenendaal.queuemusicservice.security.model

import java.util.UUID

data class QueueMusicPrincipalAuthentication(
    val qmId: UUID,
    val deviceId: String?
)