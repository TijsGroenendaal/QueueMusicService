package nl.tijsgroenendaal.sessionservice.commands

data class AddSessionSongControllerCommand(
	val trackId: String?,
	val trackAlbum: String,
	val trackName: String,
	val trackArtists: List<String>,
)