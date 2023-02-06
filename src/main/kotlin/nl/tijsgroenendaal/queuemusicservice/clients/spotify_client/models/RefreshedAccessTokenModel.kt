package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models

import com.fasterxml.jackson.annotation.JsonAlias

data class RefreshedAccessTokenModel(
    @JsonAlias("access_token")
    val accessToken: String,
    @JsonAlias("token_type")
    val tokenType: String,
    @JsonAlias("expires_in")
    val expiresIn: Int,
    @JsonAlias("scope")
    val scope: String
)