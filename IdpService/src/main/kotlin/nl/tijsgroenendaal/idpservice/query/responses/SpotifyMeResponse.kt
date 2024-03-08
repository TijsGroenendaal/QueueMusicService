package nl.tijsgroenendaal.idpservice.query.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class SpotifyMeResponse(
	val id: String,
	@JsonProperty("display_name")
	val displayName: String,
	val email: String,
	val images: List<Image>
) {
	data class Image(
		val url: String,
		val height: Int,
		val width: Int
	)
}


