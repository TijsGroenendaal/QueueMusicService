package nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.responses.track

data class GetTrackQueryResponse(
    val id: String,
    val name: String,
    val artists: List<GetTrackQueryResponseArtist>,
    val album: GetTrackQueryResponseAlbum
)

data class GetTrackQueryResponseArtist(
    val name: String
)

data class GetTrackQueryResponseAlbum(
    val name: String
)

fun List<GetTrackQueryResponseArtist>.concatArtistNames(): String {
    return this.fold("") { sum, item -> "$sum, $item" }
}
