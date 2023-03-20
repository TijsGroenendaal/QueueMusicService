package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth

import com.fasterxml.jackson.annotation.JsonAlias

data class RefreshedAccessTokenResponseModel(
    @JsonAlias("access_token")
    val accessToken: String,
    @JsonAlias("token_type")
    val tokenType: String,
    @JsonAlias("expires_in")
    val expiresIn: Long,
    @JsonAlias("scope")
    val scope: String
)