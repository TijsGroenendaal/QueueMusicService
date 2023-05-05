package nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query

data class CredentialsTokenQuery(
    val grantType: String = "client_credentials"
) {
    fun toForm(): Map<String, String> {
        return mapOf(
            Pair("grant_type", grantType)
        )
    }
}
