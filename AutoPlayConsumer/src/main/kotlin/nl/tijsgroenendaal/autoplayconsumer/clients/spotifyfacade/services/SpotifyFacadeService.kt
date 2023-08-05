package nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.services

import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.commands.AddPlaylistTrackCommand
import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.commands.DeletePlaylistTrackCommand
import nl.tijsgroenendaal.autoplayconsumer.commands.AutoplayUpdateTask

import org.springframework.stereotype.Service

@Service
class SpotifyFacadeService(
    private val spotifyFacadeClient: SpotifyFacadeClient
) {

    fun addPlaylistTrack(task: AutoplayUpdateTask) {
        println("Adding track ${task.trackId} in playlist ${task.playlistId} at position ${task.position}")

        val command = AddPlaylistTrackCommand(
            task.host,
            task.trackId,
            task.position
        )

        spotifyFacadeClient.addPlaylistTrack(task.playlistId, command)
    }

    fun deletePlaylistTrack(task: AutoplayUpdateTask) {
        println("Deleting track ${task.trackId} in playlist ${task.playlistId} at position ${task.position}")

        val command = DeletePlaylistTrackCommand(
            task.host,
            task.trackId
        )

        spotifyFacadeClient.deletePlaylistTrack(task.playlistId, command)
    }

}