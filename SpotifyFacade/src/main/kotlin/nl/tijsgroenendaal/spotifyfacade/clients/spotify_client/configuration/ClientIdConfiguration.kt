package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

import java.util.Base64

@ConfigurationProperties("queuemusic.spotify")
data class ClientIdConfiguration(
    val id: String,
    val secret: String
) {
    fun getBasicAuth(): String {
        return Base64.getEncoder().encodeToString("$id:$secret".toByteArray())
    }
}