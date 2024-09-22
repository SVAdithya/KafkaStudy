package com.kafka.learn.kafkastudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    E5001_M("MONGO_DB_EXCEPTION :"),
    E5002_K("KAFKA_EXCEPTION :"),
    E5003_GEN("APPLICATION_EXCEPTION :"),
    E5004_DB("DATABASE_CONNECTION_EXCEPTION :");

    String value;
}
