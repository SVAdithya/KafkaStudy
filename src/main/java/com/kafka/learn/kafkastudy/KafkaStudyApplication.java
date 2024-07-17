package com.kafka.learn.kafkastudy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
public class KafkaStudyApplication {
	private static final Logger logger = LoggerFactory.getLogger(KafkaStudyApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KafkaStudyApplication.class, args);
		logger.info("Main loaded");
	}

}
