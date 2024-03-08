package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.users

import com.fasterxml.jackson.annotation.JsonProperty

data class GetMeQueryResponse(
    val id: String,
    @JsonProperty("display_name")
    val displayName: String,
    val email: String,
    val images: List<Image>
) {
    class Image(
        val url: String,
        val height: Int,
        val width: Int
    )
}