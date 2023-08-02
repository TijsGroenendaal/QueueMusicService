package nl.tijsgroenendaal.idpservice.commands

import com.fasterxml.jackson.annotation.JsonProperty

data class GenerateClientTokenCommand(
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String
)
