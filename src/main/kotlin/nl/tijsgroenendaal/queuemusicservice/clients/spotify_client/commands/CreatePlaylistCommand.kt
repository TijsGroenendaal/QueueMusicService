package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.commands

data class CreatePlaylistCommand(
    val name: String,
    val description: String,
    val public: Boolean = false,
    val collaborative: Boolean = false,
)
