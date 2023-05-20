package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.tracks

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
