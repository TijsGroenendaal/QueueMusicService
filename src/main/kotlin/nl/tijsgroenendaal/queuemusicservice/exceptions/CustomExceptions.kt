package nl.tijsgroenendaal.queuemusicservice.exceptions

class AccessTokenExpiredException(): Exception()
class UnAuthorizedException(): Exception()
class NotFoundException(id: String): Exception()
class InvalidJwtException(): Exception()
class InvalidJwtSubjectException(val id: String): Exception()