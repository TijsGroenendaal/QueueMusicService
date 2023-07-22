package nl.tijsgroenendaal.queuemusicfacade.services.commands

import java.util.UUID

data class AutoplayUpdateTask(
    val host: UUID,
    val trackId: String,
    val playlistId: String,
    val position: Int,
    val oldPosition: Int?,
    val type: AutoplayUpdateTaskType
)

enum class AutoplayUpdateTaskType {
    MOVE,
    ADD
}