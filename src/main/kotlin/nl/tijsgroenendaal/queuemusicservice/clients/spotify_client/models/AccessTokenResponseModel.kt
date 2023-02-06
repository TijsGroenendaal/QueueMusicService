package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models

import com.fasterxml.jackson.annotation.JsonAlias

data class AccessTokenResponseModel(
    @JsonAlias("access_token")
    val accessToken: String,
    @JsonAlias("refresh_token")
    val refreshToken: String,
    @JsonAlias("expires_in")
    val expiresIn: Long,
    @JsonAlias("token_type")
    val tokenType: String,
    @JsonAlias("scope")
    val scope: String
)