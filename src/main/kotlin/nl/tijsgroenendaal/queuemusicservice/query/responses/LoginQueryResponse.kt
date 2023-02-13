package nl.tijsgroenendaal.queuemusicservice.query.responses

import com.fasterxml.jackson.annotation.JsonProperty
import nl.tijsgroenendaal.queuemusicservice.helper.JwtTokenUtil.Companion.JWT_TOKEN_VALIDITY

data class LoginQueryResponse(
    val jwt: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Long = JWT_TOKEN_VALIDITY
)
