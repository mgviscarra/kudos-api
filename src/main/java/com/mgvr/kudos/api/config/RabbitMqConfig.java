package com.mgvr.kudos.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mgvr.kudos.api.messaging.Receiver;

@Configuration
public class RabbitMqConfig {
	public static final String EXCHANGE_NAME = "test";
    public static final String ROUTING_KEY = "test";
 
    public static final String QUEUE_NAME = "test";
    private static final boolean IS_DURABLE_QUEUE = false;
 
    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, IS_DURABLE_QUEUE);
    }
 
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
 
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
 
    @Bean
    Receiver receiver() {
        return new Receiver();
    }

}
