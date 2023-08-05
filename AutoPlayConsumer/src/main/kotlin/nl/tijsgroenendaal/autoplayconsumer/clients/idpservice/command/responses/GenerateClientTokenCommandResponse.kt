package nl.tijsgroenendaal.autoplayconsumer.clients.idpservice.command.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class GenerateClientTokenCommandResponse(
    val token: String,
    @JsonProperty("expires_in")
    val expiresIn: Long
)
