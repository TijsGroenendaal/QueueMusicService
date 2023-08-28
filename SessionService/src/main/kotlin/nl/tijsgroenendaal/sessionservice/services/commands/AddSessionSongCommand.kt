package nl.tijsgroenendaal.sessionservice.services.commands

import nl.tijsgroenendaal.sessionservice.entity.SessionModel

import java.util.UUID

data class AddSessionSongCommand(
	val trackId: String?,
	val trackAlbum: String,
	val trackName: String,
	val trackArtists: String,
	val session: SessionModel,
	val userId: UUID
)
