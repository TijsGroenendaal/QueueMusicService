package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth

import com.fasterxml.jackson.annotation.JsonAlias

data class CredentialsTokenResponseModel(
    @JsonAlias("access_token")
    val accessToken: String,
    @JsonAlias("expires_in")
    val expiresIn: Long,
    @JsonAlias("token_type")
    val tokenType: String,
)