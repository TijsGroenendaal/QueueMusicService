package nl.tijsgroenendaal.qumu.helper

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException

import kotlinx.serialization.json.Json

import feign.FeignException

class BadRequestSerializer {
    companion object {
        fun getBadRequestException(e: FeignException): BadRequestException {
            return try {
                Json.decodeFromString<BadRequestException>(String(e.responseBody().orElseThrow { e }.array()))
            } catch(serializationException: Exception) {
                throw e
            }
        }
    }

}