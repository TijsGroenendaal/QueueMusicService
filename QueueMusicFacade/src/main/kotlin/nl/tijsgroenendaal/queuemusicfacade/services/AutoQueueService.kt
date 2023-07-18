package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.services.commands.AutoqueueUpdateTask
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AutoQueueService(
    private val rabbitTemplate: RabbitTemplate,
    @Value("\${queuemusic.rabbitmq.exchange}")
    private val exchange: String,
    @Value("\${queuemusic.rabbitmq.autoqueue.routingKey}")
    private val routingKey: String
) {

    fun publish(task: AutoqueueUpdateTask) {
        rabbitTemplate.convertAndSend(exchange, routingKey, task)
    }
}