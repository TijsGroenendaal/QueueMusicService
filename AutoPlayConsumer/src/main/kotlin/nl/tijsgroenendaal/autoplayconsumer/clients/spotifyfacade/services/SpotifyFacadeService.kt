package nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.services

import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.commands.QueueTrackCommand
import nl.tijsgroenendaal.autoplayconsumer.commands.AutoplayUpdateTask

import org.springframework.stereotype.Service

@Service
class SpotifyFacadeService(
    private val spotifyFacadeClient: SpotifyFacadeClient
) {

    fun queueTrack(message: AutoplayUpdateTask) {
        println("Adding track ${message.trackId} to queue of host ${message.hostId}")

        spotifyFacadeClient.queueTrack(QueueTrackCommand(
            hostId = message.hostId,
            trackId = message.trackId
        ))
    }
}