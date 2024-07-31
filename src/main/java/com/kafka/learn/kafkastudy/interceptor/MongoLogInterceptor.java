package com.kafka.learn.kafkastudy.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

@Configuration
public class MongoLogInterceptor extends AbstractMongoEventListener<Object> {
	private static final Logger logger = LoggerFactory.getLogger(MongoLogInterceptor.class);

	@Override
	public void onAfterSave(AfterSaveEvent<Object> event) {
		logger.info("Mongo Save Event {}, {}", event.getSource(), event);
		// Events captured for this - [BeforeConvertEvent, BeforeSaveEvent, AfterSaveEvent]
	}
}
