package nl.tijsgroenendaal.autoplayconsumer.commands

import com.fasterxml.jackson.annotation.JsonProperty

import java.util.UUID

data class AutoplayUpdateTask(
    @JsonProperty("hostId")
    val hostId: UUID,
    @JsonProperty("trackId")
    val trackId: String
)