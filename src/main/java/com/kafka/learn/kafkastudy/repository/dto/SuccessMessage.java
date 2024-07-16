package com.kafka.learn.kafkastudy.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "successMessage")
@Getter
@Setter
@AllArgsConstructor
public class SuccessMessage {
	@Id
	private String id;
	private String headers;
	private String body;
	private Long offset;
	private String time;
}
