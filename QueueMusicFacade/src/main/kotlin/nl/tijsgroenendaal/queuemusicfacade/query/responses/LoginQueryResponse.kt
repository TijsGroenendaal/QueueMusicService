package nl.tijsgroenendaal.queuemusicfacade.query.responses

import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil.Companion.JWT_TOKEN_VALIDITY

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginQueryResponse(
    val jwt: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Long = JWT_TOKEN_VALIDITY
)
