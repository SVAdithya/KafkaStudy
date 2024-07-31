package com.kafka.learn.kafkastudy.repository.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "successMessage")
public record SuccessMessage(
		@Id String id,
		String headers,
		String body,
		Long offset,
		String time
) {
}
