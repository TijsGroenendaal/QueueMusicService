package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands

data class DeletePlaylistTrackCommand(
    val uris: Array<String>
)
