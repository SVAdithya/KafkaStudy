package com.kafka.learn.kafkastudy.listener;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;

@Component
public class RegularKafkaListener {

	@KafkaListener(topics = "#{'${kafka.regular.topic}'}", containerFactory = "regularKafkaListenerContainerFactory")
	public void consume(String message) {
		System.out.println("Consumed message: " + message);
	}
}
