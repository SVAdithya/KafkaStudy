package com.kafka.learn.kafkastudy.listener;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;


@Service
@AllArgsConstructor
public class ReactiveKafkaListener implements CommandLineRunner {
	@Autowired
	@Qualifier("reactiveReceiver") KafkaReceiver kafkaReceiver;

	public void consumeMessages() {
		Flux<ReceiverRecord<Integer, String>> inboundFlux = kafkaReceiver.receive();
		inboundFlux.subscribe(r -> {
			System.out.printf("Reactive message: %s\n", r);
			r.receiverOffset().acknowledge();
		});
	}

	@Override
	public void run(String... args) throws Exception {
		consumeMessages();
	}
}


