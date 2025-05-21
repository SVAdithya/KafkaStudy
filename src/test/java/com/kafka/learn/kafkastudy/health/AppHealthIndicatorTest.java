package com.kafka.learn.kafkastudy.health;

import com.kafka.learn.kafkastudy.controller.model.AppService;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppHealthIndicatorTest {

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Mock
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @InjectMocks
    private AppHealthIndicator appHealthIndicator;

    @Mock
    private MessageListenerContainer regularKafkaListenerContainer;

    private final String MONGO_PING_COMMAND = "{ ping: 1 }";

    @BeforeEach
    void setUp() {
        // No specific common setup needed beyond MockitoAnnotations.openMocks(this); from @ExtendWith
    }

    private void mockMongoHealth(boolean isUp) {
        if (isUp) {
            when(reactiveMongoTemplate.executeCommand(MONGO_PING_COMMAND))
                    .thenReturn(Mono.just(new Document("ok", 1.0)));
        } else {
            // This simulates an error during command execution, handled by onErrorResume in mongoHealth()
            when(reactiveMongoTemplate.executeCommand(MONGO_PING_COMMAND))
                    .thenReturn(Mono.error(new RuntimeException("MongoDB connection failed")));
        }
    }
    
    private void mockMongoExecuteCommandThrowsException() {
        when(reactiveMongoTemplate.executeCommand(MONGO_PING_COMMAND))
                .thenThrow(new RuntimeException("Unexpected Mongo error"));
    }


    private void mockRegularKafkaHealth(boolean isUp) {
        when(kafkaListenerEndpointRegistry.getListenerContainer("regularKafkaListener"))
                .thenReturn(regularKafkaListenerContainer);
        when(regularKafkaListenerContainer.isRunning()).thenReturn(isUp);
    }

    private void mockRegularKafkaNotAvailable() {
        when(kafkaListenerEndpointRegistry.getListenerContainer("regularKafkaListener"))
                .thenReturn(null);
    }

    private void mockKafkaGetListenerContainerThrowsException() {
         when(kafkaListenerEndpointRegistry.getListenerContainer("regularKafkaListener"))
                .thenThrow(new RuntimeException("Unexpected Kafka error"));
    }

    @Test
    void health_allServicesUp_shouldReturnUp() {
        // Given
        mockMongoHealth(true);
        mockRegularKafkaHealth(true);

        // When
        Health health = appHealthIndicator.health();

        // Then
        assertEquals(Status.UP, health.getStatus());
        assertEquals(Status.UP.getCode(), health.getDetails().get(AppService.MONGO.name()));
        assertEquals(Status.UP.getCode(), health.getDetails().get(AppService.REGULAR_KAFKA.name()));
    }

    @Test
    void health_regularKafkaDown_mongoUp_shouldReturnUpWithDetails() {
        // Given
        mockMongoHealth(true);
        mockRegularKafkaHealth(false);

        // When
        Health health = appHealthIndicator.health();

        // Then
        assertEquals(Status.UP, health.getStatus());
        assertEquals(Status.DOWN.getCode(), health.getDetails().get(AppService.REGULAR_KAFKA.name()));
        assertEquals(Status.UP.getCode(), health.getDetails().get(AppService.MONGO.name()));
    }

    @Test
    void health_regularKafkaNotAvailable_mongoUp_shouldReturnUpWithDetails() {
        // Given
        mockMongoHealth(true);
        mockRegularKafkaNotAvailable();

        // When
        Health health = appHealthIndicator.health();

        // Then
        assertEquals(Status.UP, health.getStatus());
        assertEquals(Status.DOWN.getCode(), health.getDetails().get(AppService.REGULAR_KAFKA.name()));
        assertEquals(Status.UP.getCode(), health.getDetails().get(AppService.MONGO.name()));
    }

    @Test
    void health_mongoDown_regularKafkaUp_shouldReturnUpWithDetails() {
        // Given
        mockMongoHealth(false); // mongoHealth's onErrorResume will handle this, returning "DOWN" string
        mockRegularKafkaHealth(true);

        // When
        Health health = appHealthIndicator.health();

        // Then
        assertEquals(Status.UP, health.getStatus()); // Overall status is UP
        assertEquals(Status.DOWN.getCode(), health.getDetails().get(AppService.MONGO.name()));
        assertEquals(Status.UP.getCode(), health.getDetails().get(AppService.REGULAR_KAFKA.name()));
    }

    @Test
    void health_bothServicesDown_shouldReturnUpWithDetails() {
        // Given
        mockMongoHealth(false);
        mockRegularKafkaHealth(false);

        // When
        Health health = appHealthIndicator.health();

        // Then
        assertEquals(Status.UP, health.getStatus()); // Overall status is UP
        assertEquals(Status.DOWN.getCode(), health.getDetails().get(AppService.MONGO.name()));
        assertEquals(Status.DOWN.getCode(), health.getDetails().get(AppService.REGULAR_KAFKA.name()));
    }

    @Test
    void health_mongoExecuteCommandThrowsException_shouldReturnUpWithMongoDownDetail() {
        // Given
        mockRegularKafkaHealth(true); // Kafka is UP
        // This exception will be caught by mongoHealth's try-catch, returning "DOWN" for mongo detail
        mockMongoExecuteCommandThrowsException();

        // When
        Health health = appHealthIndicator.health();

        // Then
        assertEquals(Status.UP, health.getStatus()); // Overall status remains UP
        assertEquals(Status.DOWN.getCode(), health.getDetails().get(AppService.MONGO.name()));
        assertEquals(Status.UP.getCode(), health.getDetails().get(AppService.REGULAR_KAFKA.name()));
    }

    @Test
    void health_kafkaGetListenerContainerThrowsException_shouldReturnUpWithKafkaDownDetail() {
        // Given
        mockMongoHealth(true); // Mongo is UP
        // This exception will be caught by regularKafkaHealth's try-catch, returning "DOWN" for kafka detail
        mockKafkaGetListenerContainerThrowsException();

        // When
        Health health = appHealthIndicator.health();

        // Then
        assertEquals(Status.UP, health.getStatus()); // Overall status remains UP
        assertEquals(Status.UP.getCode(), health.getDetails().get(AppService.MONGO.name()));
        assertEquals(Status.DOWN.getCode(), health.getDetails().get(AppService.REGULAR_KAFKA.name()));
    }
}
