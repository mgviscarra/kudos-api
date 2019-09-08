package com.mgvr.kudos.api.messaging;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.mgvr.kudos.api.KudosApiApplication;

@Component
public class Sender {
	private final RabbitTemplate rabbitTemplate;
	private final Receiver receiver;
	
	public Sender(Receiver receiver, RabbitTemplate rabbitTemplate) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void send() {
		System.out.println("Sending message...");
		 rabbitTemplate.convertAndSend(KudosApiApplication.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
		 try {
			receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
