package nl.tijsgroenendaal.sessionservice.services

import nl.tijsgroenendaal.sessionservice.services.commands.UserEventTask

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value

import org.springframework.stereotype.Service

@Service
class UserEventService(
	private val rabbitTemplate: RabbitTemplate,
	@Value("\${queuemusic.rabbitmq.userevent.exchange}")
	private val exchange: String,
	@Value("\${queuemusic.rabbitmq.userevent.queueName}")
	private val queue: String
) {

	fun publish(task: UserEventTask) {
		rabbitTemplate.convertAndSend(exchange, queue, task)
	}
}