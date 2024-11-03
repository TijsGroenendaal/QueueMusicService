package nl.tijsgroenendaal.sessionservice.queue.client

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfiguration {

    @Value("\${queuemusic.rabbitmq.autoqueue.exchange}")
    private lateinit var autoplayExchange: String

    @Value("\${queuemusic.rabbitmq.autoqueue.queueName}")
    private lateinit var autoplayQueue: String

    @Value("\${queuemusic.rabbitmq.autoqueue.routingKey}")
    private lateinit var autoplayRouting: String

    @Value("\${queuemusic.rabbitmq.userevent.exchange}")
    private lateinit var userEventExchange: String

    @Bean
    fun autoplayQueue(): Queue = Queue(autoplayQueue, false)

    @Bean
    fun autoplayExchange(): DirectExchange = DirectExchange(autoplayExchange, true, false)

    @Bean
    fun autoplayBinding(@Qualifier("autoplayQueue") queue: Queue, exchange: DirectExchange): Binding =
        BindingBuilder.bind(queue).to(exchange).with(autoplayRouting)

    @Bean
    fun userEventExchange(): FanoutExchange = FanoutExchange(userEventExchange, true, false)

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = producerJackson2MessageConverter()
        return rabbitTemplate
    }

    @Bean
    fun producerJackson2MessageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }
}