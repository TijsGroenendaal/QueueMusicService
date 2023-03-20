package nl.tijsgroenendaal.spotifyfacade.exceptions

class AccessTokenExpiredException(): Exception()
class BadRequestException(val code: String, message: String): Exception(message)