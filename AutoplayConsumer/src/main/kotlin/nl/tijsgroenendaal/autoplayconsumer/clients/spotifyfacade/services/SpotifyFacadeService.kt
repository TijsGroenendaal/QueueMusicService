package nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.services

import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.autoplayconsumer.consumers.model.SessionSongAddedCommand
import org.springframework.stereotype.Service

@Service
class SpotifyFacadeService(
    private val spotifyFacadeClient: SpotifyFacadeClient
) {

    fun addSong(command: SessionSongAddedCommand) {
        spotifyFacadeClient.addSessionSong(command.sessionId, command.songId)
    }

}