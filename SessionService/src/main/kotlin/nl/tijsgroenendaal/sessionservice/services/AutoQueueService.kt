package nl.tijsgroenendaal.sessionservice.services

import nl.tijsgroenendaal.sessionservice.services.commands.AutoplayUpdateTask

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AutoQueueService(
    private val rabbitTemplate: RabbitTemplate,
    @Value("\${queuemusic.rabbitmq.autoqueue.exchange}")
    private val exchange: String,
    @Value("\${queuemusic.rabbitmq.autoqueue.routingKey}")
    private val routingKey: String
) {

    fun publish(task: AutoplayUpdateTask) {
        rabbitTemplate.convertAndSend(exchange, routingKey, task)
    }
}