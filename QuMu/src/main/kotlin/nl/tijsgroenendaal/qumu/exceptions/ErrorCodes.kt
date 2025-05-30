package nl.tijsgroenendaal.qumu.exceptions

enum class AuthErrorCodes(
    private val code: String,
    private val status: Int,
    private val message: String
): ErrorCode {
    LINK_ACCESS_TOKEN_EXPIRED("AUTH0001", 401, "Userlink AccessToken has expired."),
    LINK_REFRESH_TOKEN_EXPIRED("AUTH0002", 401, "Userlink RefreshToken has expired."),
    INVALID_JWT_SUBJECT("AUTH0003", 401, "Subject is invalid"),
    INVALID_JWT_REFRESH_TOKEN("AUTH0004", 401, "RefreshToken is not valid."),
    INVALID_JWT("AUTH0004", 401, "Jwt is not valid"),
    UNABLE_TO_LOGIN_TO_LINK("AUTH0005", 400, "Unable to login to link account"),
    ANONYMOUS_CLIENT_CLAIMS("AUTH0006", 400, "Anonymous client claims");

    override fun getCode(): String {
        return this.code
    }

    override fun getStatus(): Int {
        return this.status
    }

    override fun getMessage(): String {
        return this.message
    }
}

enum class RegisteredClientErrorCodes(
    private val code: String,
    private val status: Int,
    private val message: String
): ErrorCode {
    CLIENT_NOT_FOUND("RGCL0001", 404, "Client not found."),
    BAD_CLIENT_CREDENTIALS("RGCL0002", 403, "Bad client Credentials.");

    override fun getCode(): String {
        return this.code
    }

    override fun getStatus(): Int {
        return this.status
    }

    override fun getMessage(): String {
        return this.message
    }
}

enum class UserErrorCodes(
    private val code: String,
    private val status: Int,
    private val message: String
): ErrorCode {
    USER_NOT_FOUND("USER0001", 404, "User not found.");

    override fun getCode(): String {
        return this.code
    }

    override fun getStatus(): Int {
        return this.status
    }

    override fun getMessage(): String {
        return this.message
    }
}

enum class UserLinkErrorCodes(
    private val code: String,
    private val status: Int,
    private val message: String
): ErrorCode {
    USER_LINK_NOT_FOUND("USLK0001", 404, "User has not signed in with Spotify.");

    override fun getCode(): String {
        return this.code
    }

    override fun getStatus(): Int {
        return this.status
    }

    override fun getMessage(): String {
        return this.message
    }
}

enum class SessionErrorCodes(
    private val code: String,
    private val status: Int,
    private val message: String
): ErrorCode {
    DURATION_EXCEEDED("SESS0001", 400, "Duration exceeds maximum."),
    HOSTING_SESSIONS_EXCEEDED("SESS0002", 400, "Maximum amount of active sessions reached."),
    SESSION_NOT_FOUND("SESS0003", 404, "Session not found."),
    SESSION_ENDED("SESS0004", 403, "Session has ended."),
    MAX_USERS_EXCEEDED("SESS0005", 400, "Session has reached it maximum users."),
    ALREADY_JOINED("SESS0006", 400, "User has already joined this session."),
    USER_NOT_JOINED("SESS0007", 403, "User has not joined this session."),
    NOT_HOST("SESS0008", 403, "User is not the host."),
    NEGATIVE_AUTOPLAY_ACCEPTANCE("SESS0009", 400, "Autoplay acceptance needs to be positive"),
    USER_ALREADY_JOINED_OTHER("SESS0010", 400, "User has already joined another session."),;

    override fun getCode(): String {
        return this.code
    }

    override fun getStatus(): Int {
        return this.status
    }

    override fun getMessage(): String {
        return this.message
    }
}

enum class DeviceLinkErrorCodes(
    private val code: String,
    private val status: Int,
    private val message: String
): ErrorCode {
    DEVICE_LINK_NOT_FOUND("DEVL0001", 404, "DeviceLink not found.");

    override fun getCode(): String {
        return this.code
    }

    override fun getStatus(): Int {
        return this.status
    }

    override fun getMessage(): String {
        return this.message
    }
}

enum class SessionSongErrorCode(
    private val code: String,
    private val status: Int,
    private val message: String
): ErrorCode {
    TRACK_NOT_FOUND("SSON0001", 404, "Spotify Track not found."),
    ADD_SONG_TIMEOUT_NOT_PASSED("SSON0002", 400, "User addSong timeout has not passed."),
    SESSION_SONG_NOT_FOUND("SSON0004", 404, "SessionSong not found"),
    SONG_ALREADY_QUEUED("SSON0005", 403, "SessionSong has already been queued");

    override fun getCode(): String {
        return this.code
    }

    override fun getStatus(): Int {
        return this.status
    }

    override fun getMessage(): String {
        return this.message
    }
}

enum class SessionSongUserVoteErrorCodes(
    private val code: String,
    private val status: Int,
    private val message: String
): ErrorCode {
    VOTE_NOT_FOUND("SSUV0001", 404, "SessionSongUserVote not found");

    override fun getCode(): String {
        return this.code
    }

    override fun getStatus(): Int {
        return this.status
    }

    override fun getMessage(): String {
        return this.message
    }
}

enum class FallbackErrorCodes(
    private val code: String,
    private val status: Int,
    private val message: String
): ErrorCode {
    INTERNAL_SERVER_ERROR("FALL0001", 500, "Internal Server Error");

    override fun getCode(): String {
        return this.code
    }

    override fun getStatus(): Int {
        return this.status
    }

    override fun getMessage(): String {
        return this.message
    }
}


interface ErrorCode {
    fun getCode(): String
    fun getStatus(): Int
    fun getMessage(): String
}