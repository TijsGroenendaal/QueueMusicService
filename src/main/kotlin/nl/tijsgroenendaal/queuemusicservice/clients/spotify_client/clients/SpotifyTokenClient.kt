package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.SpotifyTokenClientConfiguration
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.RefreshedAccessTokenModel

import com.fasterxml.jackson.annotation.JsonProperty

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    url = "\${clients.spotify-api}/v1/token",
    name = "spotify-token-client",
    configuration = [SpotifyTokenClientConfiguration::class]
)
interface SpotifyTokenClient {

    @PostMapping
    fun getRefreshAccessToken(
        @RequestBody tokenRequest: RefreshTokenRequest
    ): RefreshedAccessTokenModel

}

data class RefreshTokenRequest(
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("grant_type")
    val grantType: String = "refresh_token"
)