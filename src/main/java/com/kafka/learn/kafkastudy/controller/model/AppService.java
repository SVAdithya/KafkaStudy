package com.kafka.learn.kafkastudy.controller.model;

public enum AppService {
    ORDER,
    PAYMENT,
    REGULAR_KAFKA,
    REACTIVE_KAFKA, // Even if commented out in AppHealthIndicator, keep for completeness
    MONGO
}
