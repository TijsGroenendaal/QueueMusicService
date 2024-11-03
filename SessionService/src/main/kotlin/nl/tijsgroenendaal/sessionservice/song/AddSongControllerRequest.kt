package nl.tijsgroenendaal.sessionservice.song

data class AddSongControllerRequest(
    val trackId: String?,
    val trackAlbum: String,
    val trackName: String,
    val trackArtists: List<String>,
)