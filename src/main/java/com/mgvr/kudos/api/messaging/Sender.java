package com.mgvr.kudos.api.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mgvr.kudos.api.config.RabbitMqConfig;

@Component
public class Sender {
	//private static final String MESSAGE = "Hello world!";
	 
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendMessage(String message) {
    	 System.out.println("[Application] Enviando el mensaje \"" + message + "\"...");
         rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, message);
    }

}
