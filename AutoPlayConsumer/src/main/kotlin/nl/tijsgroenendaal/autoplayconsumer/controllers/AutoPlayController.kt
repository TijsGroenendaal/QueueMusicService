package nl.tijsgroenendaal.autoplayconsumer.controllers

import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.services.SpotifyFacadeService
import nl.tijsgroenendaal.autoplayconsumer.commands.AutoplayUpdateTask

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class AutoPlayController(
    private val spotifyFacadeService: SpotifyFacadeService
) {

    @RabbitListener(queues = ["\${queuemusic.rabbitmq.autoqueue.queueName}"])
    fun autoplay(message: AutoplayUpdateTask) {
        spotifyFacadeService.queueTrack(message)
    }
}