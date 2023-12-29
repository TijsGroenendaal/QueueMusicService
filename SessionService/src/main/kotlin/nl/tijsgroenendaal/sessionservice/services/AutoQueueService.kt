package nl.tijsgroenendaal.sessionservice.services

import nl.tijsgroenendaal.sessionservice.services.commands.AutoplayUpdateTask

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AutoQueueService(
    private val rabbitTemplate: RabbitTemplate,
    private val exchange: DirectExchange,
    @Value("\${queuemusic.rabbitmq.autoqueue.routingKey}")
    private val routingKey: String
) {

    fun publish(task: AutoplayUpdateTask) {
        rabbitTemplate.convertAndSend(exchange.name, routingKey, task)
    }
}