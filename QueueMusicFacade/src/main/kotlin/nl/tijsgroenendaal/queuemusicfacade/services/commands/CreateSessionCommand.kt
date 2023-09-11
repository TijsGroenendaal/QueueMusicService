package nl.tijsgroenendaal.queuemusicfacade.services.commands

data class CreateSessionCommand(
		val duration: Long,
		val maxUsers: Int = 10,
		val autoplay: CreateSessionCommandAutoplay?
)

data class CreateSessionCommandAutoplay(
		val playlistId: String,
		val acceptance: Int
)
