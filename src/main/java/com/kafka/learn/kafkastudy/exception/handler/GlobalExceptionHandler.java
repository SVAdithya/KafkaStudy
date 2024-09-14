package com.kafka.learn.kafkastudy.exception.handler;


import com.kafka.learn.kafkastudy.exception.ExceptionCode;
import com.mongodb.MongoException;
import org.apache.kafka.common.KafkaException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ExceptionCode.E5003_GEN.getValue() + ex.getMessage());
        response.put("status", ExceptionCode.E5003_GEN.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle data access exceptions (for MongoDB or other databases)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ExceptionCode.E5004_DB.getValue() + ex.getMessage());
        response.put("status", ExceptionCode.E5004_DB.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle Kafka exceptions
    @ExceptionHandler(KafkaException.class)
    public ResponseEntity<Object> handleKafkaException(KafkaException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ExceptionCode.E5002_K.getValue() + ex.getMessage());
        response.put("status", ExceptionCode.E5002_K.toString());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Handle MongoDB exceptions
    @ExceptionHandler(MongoException.class)
    public ResponseEntity<Object> handleMongoException(MongoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ExceptionCode.E5001_M.getValue() + ex.getMessage());
        response.put("status", ExceptionCode.E5001_M.toString());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
