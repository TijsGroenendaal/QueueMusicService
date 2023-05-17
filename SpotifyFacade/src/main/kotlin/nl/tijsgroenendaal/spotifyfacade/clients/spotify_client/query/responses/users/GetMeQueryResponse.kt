package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.users

data class GetMeQueryResponse(
    val id: String,
    val email: String,
    val images: Any,
    val followers: Any
)