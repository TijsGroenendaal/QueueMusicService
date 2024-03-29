package nl.tijsgroenendaal.idpservice.query.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginQueryResponse(
    val token: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Long
)
