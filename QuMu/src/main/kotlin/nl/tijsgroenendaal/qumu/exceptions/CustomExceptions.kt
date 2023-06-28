package nl.tijsgroenendaal.qumu.exceptions

import kotlinx.serialization.Serializable
import java.lang.RuntimeException

class AccessTokenExpiredException : BadRequestException(AuthErrorCodes.LINK_ACCESS_TOKEN_EXPIRED)
class RefreshTokenExpiredException: BadRequestException(AuthErrorCodes.LINK_REFRESH_TOKEN_EXPIRED)
class InvalidJwtException : BadRequestException(AuthErrorCodes.INVALID_JWT)
class InvalidRefreshJwtException : BadRequestException(AuthErrorCodes.INVALID_JWT_REFRESH_TOKEN)

@Serializable
open class BadRequestException(
    val code: String,
    val status: Int,
    override val message: String
): RuntimeException() {

    constructor(error: ErrorCode): this(
        error.getCode(),
        error.getStatus(),
        error.getMessage()
    )

    fun isError(error: ErrorCode): Boolean {
        return code == error.getCode()
    }
}