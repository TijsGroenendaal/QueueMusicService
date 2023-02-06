package nl.tijsgroenendaal.queuemusicservice.exceptions

class AccessTokenExpiredException(): Exception()
class UnAuthorizedException(): Exception()
class UnAuthenticatedException(): Exception()
class NotFoundException(id: String): Exception()