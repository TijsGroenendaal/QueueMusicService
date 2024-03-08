package nl.tijsgroenendaal.idpservice.facades

import nl.tijsgroenendaal.idpservice.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.idpservice.query.responses.SpotifyMeResponse
import nl.tijsgroenendaal.qumu.helper.catchingFeignRequest
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject
import org.springframework.stereotype.Service

@Service
class UserFacade(
	private val spotifyFacadeClient: SpotifyFacadeClient
) {

	fun me(): SpotifyMeResponse {
		val userId = getAuthenticationContextSubject()

		return catchingFeignRequest { spotifyFacadeClient.getSpotifyUserById(userId) }
			.let {
				SpotifyMeResponse(
					it.id,
					it.displayName,
					it.email,
					it.images.map { image ->
						SpotifyMeResponse.Image(
							image.url,
							image.height,
							image.width
						)
					}
				)
			}
	}

}