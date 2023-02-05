package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models

data class RefreshedAccessTokenModel(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val scope: String
)