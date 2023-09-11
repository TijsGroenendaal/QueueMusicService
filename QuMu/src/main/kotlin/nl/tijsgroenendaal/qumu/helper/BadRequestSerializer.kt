package nl.tijsgroenendaal.qumu.helper

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException

import kotlinx.serialization.json.Json

import feign.FeignException

fun getBadRequestException(e: FeignException): BadRequestException {
    return try {
        Json.decodeFromString<BadRequestException>(String(e.responseBody().orElseThrow { e }.array()))
    } catch(serializationException: Exception) {
        throw e
    }
}

fun <T> catchingFeignRequest(request: () -> T): T {
    try {
        return request.invoke()
    } catch (e: FeignException) {
        throw getBadRequestException(e)
    }
}