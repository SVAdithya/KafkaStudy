package com.kafka.learn.kafkastudy.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.List;


@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {
	@Value("${spring.kafka.autostart:false}")
	private Boolean autoStart;

	@Bean(name = "regularKafkaProperties")
	@ConfigurationProperties(prefix="spring.kafka")
	public KafkaProperties kafkaProperties() {
		return new KafkaProperties();
	}

	@Bean("regularKafkaConsumerFactory")
	public ConsumerFactory<String, String> consumerFactory(
			@Qualifier("regularKafkaProperties") KafkaProperties kafkaProperties
	) {
		return new DefaultKafkaConsumerFactory<>(kafkaProperties.getConsumer().buildProperties(null));
	}

	@Bean("regularKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
			@Qualifier("regularKafkaConsumerFactory") ConsumerFactory consumerFactory
	) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setAutoStartup(autoStart);
		factory.setConcurrency(5);
		factory.setRecordFilterStrategy(record -> {
			// accepts data with false condition, true condition data will be filtered out.
			return record.value().startsWith("test");
		});
		return factory;
	}
}
