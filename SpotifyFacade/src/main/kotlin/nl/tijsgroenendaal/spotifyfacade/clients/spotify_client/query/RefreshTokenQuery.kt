package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query

data class RefreshTokenQuery(
    val refreshToken: String,
    val grantType: String = "refresh_token"
) {
    fun toForm(): Map<String, Any> {
        return mapOf(
            Pair("refresh_token", refreshToken),
            Pair("grant_type", grantType)
        )
    }
}