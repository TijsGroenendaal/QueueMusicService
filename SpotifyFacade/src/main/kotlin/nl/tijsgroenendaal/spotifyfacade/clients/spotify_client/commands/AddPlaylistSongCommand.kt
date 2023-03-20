package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands

data class AddPlaylistSongCommand(
    val uris: List<String>,
    val position: Int
)
