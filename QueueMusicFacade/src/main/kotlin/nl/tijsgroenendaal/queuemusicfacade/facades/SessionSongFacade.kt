package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services.SpotifyService
import nl.tijsgroenendaal.queuemusicfacade.commands.AcceptSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSpotifySessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.DeleteSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.VoteSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.AddSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.VoteSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.services.SessionService
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject

import org.springframework.stereotype.Service

@Service
class SessionSongFacade(
    private val spotifyService: SpotifyService,
    private val sessionService: SessionService
) {

    fun addSpotifySessionSong(command: AddSpotifySessionSongCommand): AddSessionSongCommandResponse {
        val track = spotifyService.getTrack(command.songId)

        return addSessionSong(AddSessionSongCommand(
            track.id,
            track.album.name,
            track.name,
            track.artists.map { it.name },
            command.sessionId
        ))
    }

    fun addSessionSong(command: AddSessionSongCommand): AddSessionSongCommandResponse {
        val userId = getAuthenticationContextSubject()

        return sessionService.createSessionSong(command, userId)
    }

    fun voteSessionSong(command: VoteSessionSongCommand): VoteSessionSongCommandResponse {
        return sessionService.vote(command, getAuthenticationContextSubject())
    }

    fun deleteSessionSong(command: DeleteSessionSongCommand) {
        sessionService.deleteSessionSong(command, getAuthenticationContextSubject())
    }

    fun acceptSessionSong(command: AcceptSessionSongCommand) {
        sessionService.acceptSessionSong(command, getAuthenticationContextSubject())
    }
}