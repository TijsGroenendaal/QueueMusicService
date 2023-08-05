package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands

data class AddPlaylistTrackCommand(
    val uris: Array<String>,
    val position: Int
)
