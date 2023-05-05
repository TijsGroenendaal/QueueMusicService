package nl.tijsgroenendaal.qumu.exceptions

class AccessTokenExpiredException(): Exception()
class UnAuthorizedException(): Exception()
open class InvalidJwtException(): Exception()
class InvalidJwtSubjectException(val id: String): InvalidJwtException()
class InvalidRefreshJwtException(): InvalidJwtException()
class BadRequestException(val code: String, message: String): Exception(message) {
    constructor(code: ErrorCode, message: String): this(code.getCode(), message)
}