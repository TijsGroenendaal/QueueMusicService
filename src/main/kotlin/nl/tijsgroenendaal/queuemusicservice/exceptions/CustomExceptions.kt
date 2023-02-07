package nl.tijsgroenendaal.queuemusicservice.exceptions

class AccessTokenExpiredException(): Exception()
class UnAuthorizedException(): Exception()
class NotFoundException(id: String): Exception()
open class InvalidJwtException(): Exception()
class InvalidJwtSubjectException(val id: String): InvalidJwtException()
class InvalidRefreshJwtException(): InvalidJwtException()