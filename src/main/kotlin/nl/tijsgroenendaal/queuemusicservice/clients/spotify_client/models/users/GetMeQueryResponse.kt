package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.users

import com.fasterxml.jackson.annotation.JsonProperty

data class GetMeQueryResponse(
    @JsonProperty("id")
    val id: String
)