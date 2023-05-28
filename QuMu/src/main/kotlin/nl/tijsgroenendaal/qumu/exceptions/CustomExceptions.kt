package nl.tijsgroenendaal.qumu.exceptions

import java.lang.RuntimeException

class AccessTokenExpiredException : BadRequestException(AuthErrorCodes.LINK_ACCESS_TOKEN_EXPIRED)
class RefreshTokenExpiredException: BadRequestException(AuthErrorCodes.LINK_REFRESH_TOKEN_EXPIRED)
class InvalidJwtException : BadRequestException(AuthErrorCodes.INVALID_JWT)
class InvalidRefreshJwtException : BadRequestException(AuthErrorCodes.INVALID_JWT_REFRESH_TOKEN)

open class BadRequestException(
    val error: ErrorCode
): RuntimeException(error.getMessage())