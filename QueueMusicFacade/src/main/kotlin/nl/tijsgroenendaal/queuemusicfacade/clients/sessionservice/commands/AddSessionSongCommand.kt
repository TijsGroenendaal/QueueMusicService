package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands

data class AddSessionSongCommand(
		val trackId: String?,
		val trackAlbum: String,
		val trackName: String,
		val trackArtists: List<String>,
)
