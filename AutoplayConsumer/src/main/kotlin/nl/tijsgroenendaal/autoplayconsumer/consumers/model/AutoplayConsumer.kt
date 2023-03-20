package nl.tijsgroenendaal.autoplayconsumer.consumers.model

import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.services.SpotifyFacadeService

import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener

@RabbitListener(queues = ["autoplay"])
class AutoplayConsumer(
    private val spotifyFacadeService: SpotifyFacadeService
) {
    @RabbitHandler
    fun consume(command: SessionSongAddedCommand) {
        spotifyFacadeService.addSong(command)
    }
}