package nl.tijsgroenendaal.sessionservice.session

data class CreateSessionControllerRequest(
    val duration: Long,
    val maxUsers: Int = 10,
    val autoplay: CreateSessionControllerRequestAutoplay?
)

data class CreateSessionControllerRequestAutoplay(
    val acceptance: Int,
)
