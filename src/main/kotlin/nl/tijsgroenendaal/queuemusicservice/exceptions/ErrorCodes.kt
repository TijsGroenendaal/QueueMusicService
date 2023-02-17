package nl.tijsgroenendaal.queuemusicservice.exceptions

enum class UserErrorCodes(
    private val code: String
): ErrorCode {
    USER_NOT_FOUND("USER0001");

    override fun getCode(): String {
        return this.code
    }
}

enum class SessionErrorCodes(
    private val code: String
): ErrorCode {
    DURATION_EXCEEDED("SESS0001"),
    HOSTING_SESSIONS_EXCEEDED("SESS0002"),
    SESSION_NOT_FOUND("SESS0003"),
    SESSION_ENDED("SESS0004"),
    NO_USERLINK_FOUND("SESS0005"),
    MAX_USERS_EXCEEDED("SESS0006"),
    ALREADY_JOINED("SESS0007");

    override fun getCode(): String {
        return this.code
    }
}

enum class DeviceLinkErrorCodes(
    private val code: String
): ErrorCode {
    DEVICE_LINK_NOT_AVAILABLE("DEVL0001");

    override fun getCode(): String {
        return this.code
    }
}

enum class SessionSongErrorCode(
    private val code: String
): ErrorCode {
    TRACK_NOT_FOUND("SSON0001"),
    ADD_SONG_TIMEOUT_NOT_PASSED("SSON0002"),
    DEVICE_NOT_JOINED("SSON0003");

    override fun getCode(): String {
        return this.code
    }
}


interface ErrorCode {
    fun getCode(): String
}