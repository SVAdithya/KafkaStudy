package com.kafka.learn.kafkastudy.listener;

import com.kafka.learn.kafkastudy.repository.SuccessMessageRepository;
import com.kafka.learn.kafkastudy.repository.dto.SuccessMessage;

import com.kafka.learn.kafkastudy.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.ZonedDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
public class ReactiveKafkaListener implements CommandLineRunner {

	private KafkaReceiver kafkaReceiver;
	private HeaderUtil headerUtil;

	private SuccessMessageRepository successMessageRepository;

	public void consumeMessages() {
		Flux<ReceiverRecord<Integer, String>> inboundFlux = kafkaReceiver.receive();
		inboundFlux.subscribe(r -> {
			System.out.printf("Reactive message: %s\n", r);
			successMessageRepository.save(new SuccessMessage(
					UUID.randomUUID().toString(),
					headerUtil.headersMapToString(r.headers()),
					r.value(), r.offset(),
					ZonedDateTime.now().toString())
			).subscribe();
			r.receiverOffset().acknowledge();
		});
	}

	@Override
	public void run(String... args) {
		consumeMessages();
	}
}


