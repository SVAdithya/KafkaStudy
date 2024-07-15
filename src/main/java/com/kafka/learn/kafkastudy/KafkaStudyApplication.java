package com.kafka.learn.kafkastudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
public class KafkaStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaStudyApplication.class, args);
	}

}
