package com.kafka.learn.kafkastudy.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

	@Bean("regularKafkaConsumerFactory")
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> consumerProps = new HashMap<>();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-3");
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(consumerProps);
	}

	@Bean("regularKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
			@Qualifier("regularKafkaConsumerFactory") ConsumerFactory consumerFactory
	) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setAutoStartup(true);
		factory.setConcurrency(5);
		factory.setRecordFilterStrategy(record -> {
			return record.value().startsWith("test");
		});
		return factory;
	}
}
