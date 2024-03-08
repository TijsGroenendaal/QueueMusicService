package nl.tijsgroenendaal.idpservice.clients.spotifyfacade.query.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class GetSpotifyMeQueryResponse(
	val id: String,
	@JsonProperty("display_name")
	val displayName: String,
	val email: String,
	val images: List<GetSpotifyMeQueryResponseImage>
) {
	data class GetSpotifyMeQueryResponseImage(
		val url: String,
		val height: Int,
		val width: Int
	)
}