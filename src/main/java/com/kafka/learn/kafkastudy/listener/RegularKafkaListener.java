package com.kafka.learn.kafkastudy.listener;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;

@Component
public class RegularKafkaListener {

	@KafkaListener(topics = "#{'${kafka.regular.topic}'}", containerFactory = "regularKafkaListenerContainerFactory")
	public void consume(Message<String> message) {

		System.out.print("Consumed message: " + message.getPayload());
		System.out.println(" Consumed header: " + message.getHeaders());
	}
}
