package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses

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
