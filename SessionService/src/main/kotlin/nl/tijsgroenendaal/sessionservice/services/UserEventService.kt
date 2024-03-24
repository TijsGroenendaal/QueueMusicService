package nl.tijsgroenendaal.sessionservice.services

import nl.tijsgroenendaal.sessionservice.services.commands.UserEventTask

import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class UserEventService(
	private val rabbitTemplate: RabbitTemplate,
	private val exchange: FanoutExchange
) {

	fun publish(task: UserEventTask) {
		rabbitTemplate.convertAndSend(exchange.name, "", task)
	}
}