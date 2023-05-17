package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query

data class AccessTokenQuery(
    val code: String,
    val redirectUri: String = "https://www.google.com",
    val grantType: String = "authorization_code"
) {
    fun toForm(): Map<String, Any> {
        return mapOf(
            Pair("code", code),
            Pair("redirect_uri", redirectUri),
            Pair("grant_type", grantType)
        )
    }
}