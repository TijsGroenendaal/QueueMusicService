package nl.tijsgroenendaal.autoplayconsumer.commands

import com.fasterxml.jackson.annotation.JsonProperty

import java.util.UUID

data class AutoplayUpdateTask(
    @JsonProperty("host")
    val host: UUID,
    @JsonProperty("trackId")
    val trackId: String,
    @JsonProperty("playlistId")
    val playlistId: String,
    @JsonProperty("position")
    val position: Int,
    @JsonProperty("oldPosition")
    val oldPosition: Int?,
    @JsonProperty("type")
    val type: AutoplayUpdateTaskType
)

enum class AutoplayUpdateTaskType {
    MOVE,
    ADD
}