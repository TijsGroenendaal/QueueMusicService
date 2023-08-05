package nl.tijsgroenendaal.autoplayconsumer.controllers

import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.services.SpotifyFacadeService
import nl.tijsgroenendaal.autoplayconsumer.commands.AutoplayUpdateTask
import nl.tijsgroenendaal.autoplayconsumer.commands.AutoplayUpdateTaskType

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class AutoPlayController(
    private val spotifyFacadeService: SpotifyFacadeService
) {

    @RabbitListener(queues = ["\${queuemusic.rabbitmq.autoqueue.queueName}"])
    fun autoplay(message: AutoplayUpdateTask) {
        when(message.type) {
            AutoplayUpdateTaskType.MOVE -> println("Moving track ${message.trackId} in playlist ${message.playlistId} to position ${message.position}")
            AutoplayUpdateTaskType.ADD -> spotifyFacadeService.addPlaylistTrack(message)
            AutoplayUpdateTaskType.DELETE -> spotifyFacadeService.deletePlaylistTrack(message)
        }
    }
}