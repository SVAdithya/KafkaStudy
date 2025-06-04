package com.kafka.learn.kafkastudy.interceptor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoLogInterceptorTest {

    @InjectMocks
    private MongoLogInterceptor mongoLogInterceptor;

    // We don't mock the logger directly, but rather verify its interactions.
    // To capture log output, one would typically use a logging framework's test appender.
    // For this test, we'll focus on the interaction with the event.
    // If specific log messages need verification, a more complex setup with a test appender would be needed.

    @Test
    @SuppressWarnings("unchecked")
    void onAfterSave_shouldLogEvent() {
        // Arrange
        Object source = new Object(); // The entity being saved
        org.bson.Document document = new org.bson.Document("key", "value"); // Dummy BSON document

        // Create a mock AfterSaveEvent
        // AfterSaveEvent is generic, so we use Object.class for this test
        AfterSaveEvent<Object> mockEvent = mock(AfterSaveEvent.class);
        when(mockEvent.getSource()).thenReturn(source); // event.getSource() is the entity
        // event.getDocument() is the BSON document, but the interceptor logs event.toString() and event.getSource()
        // The current interceptor logs event.getSource() and event (which calls event.toString())
        // So, we don't strictly need to mock event.getDocument() unless event.toString() uses it and is complex.
        // The default toString for AfterSaveEvent might be simple enough.

        // Act
        mongoLogInterceptor.onAfterSave(mockEvent);

        // Assert
        // We can't directly assert logger output without a test appender.
        // However, we can verify that methods on the event object were called if the logger uses them.
        // The logger call is: logger.info("Mongo Save Event {}, {}", event.getSource(), event);
        // This means event.getSource() is called, and event.toString() is implicitly called by SLF4J.

        // Verify that getSource() was called on the event.
        verify(mockEvent, times(1)).getSource();

        // We can't easily verify logger.info(...) was called with specific arguments without a log appender
        // or by injecting a mock logger. Since the logger is static in MongoLogInterceptor,
        // we would need PowerMockito or a similar tool to mock it, or refactor the class
        // to make the logger injectable.

        // For now, this test ensures the method runs and interacts with the event as expected.
        // Achieving 100% coverage for this line would ideally involve verifying the log output.
    }
}
