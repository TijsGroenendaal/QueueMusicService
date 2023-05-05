package nl.tijsgroenendaal.queuemusicfacade.security.model

import java.util.UUID

data class QueueMusicPrincipalAuthentication(
    val qmId: UUID,
    val deviceId: String?
)