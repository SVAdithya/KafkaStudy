package com.kafka.learn.kafkastudy.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;

@Configuration
public class MongoLogInterceptor extends AbstractMongoEventListener<Object> {
	private static final Logger logger = LoggerFactory.getLogger(MongoLogInterceptor.class);

	@Override
	public void onApplicationEvent(MongoMappingEvent<?> event) {
		logger.info("Mongo Event {}, {}", event.getSource(), event);
		// Events captured for this - [BeforeConvertEvent, BeforeSaveEvent, AfterSaveEvent]
	}
}

