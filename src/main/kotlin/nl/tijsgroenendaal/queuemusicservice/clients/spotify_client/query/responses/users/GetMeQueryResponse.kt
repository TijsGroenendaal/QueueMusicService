package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.users

data class GetMeQueryResponse(
    val id: String,
    val email: String,
    val images: Any,
    val followers: Any
)