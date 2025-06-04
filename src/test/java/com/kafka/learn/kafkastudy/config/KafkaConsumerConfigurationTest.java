package com.kafka.learn.kafkastudy.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.messaging.MessageChannel;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerConfigurationTest {

    private KafkaConsumerConfiguration kafkaConsumerConfiguration;

    @Mock
    private KafkaProperties mockKafkaProperties;

    @Mock
    private KafkaProperties.Consumer mockKafkaConsumerProperties;

    @Mock
    private KafkaProperties.Template mockKafkaTemplateProperties; 

    @BeforeEach
    void setUp() {
        kafkaConsumerConfiguration = new KafkaConsumerConfiguration();
    }

    @Test
    void errorChannel_shouldReturnNonNullPublishSubscribeChannel() {
        MessageChannel channel = kafkaConsumerConfiguration.errorChannel();
        assertNotNull(channel);
        assertTrue(channel instanceof PublishSubscribeChannel);
    }

    @Test
    void kafkaProperties_shouldReturnNonNullKafkaProperties() {
        KafkaProperties properties = kafkaConsumerConfiguration.kafkaProperties();
        assertNotNull(properties);
    }

    @Test
    @SuppressWarnings("unchecked")
    void consumerFactory_shouldReturnConfiguredConsumerFactory() {
        // Arrange
        Map<String, Object> props = new HashMap<>(); // Empty map as per instruction
        
        when(mockKafkaProperties.getConsumer()).thenReturn(mockKafkaConsumerProperties);
        when(mockKafkaConsumerProperties.buildProperties(null)).thenReturn(props);

        // Act
        ConsumerFactory<String, String> factory = kafkaConsumerConfiguration.consumerFactory(mockKafkaProperties);

        // Assert
        assertNotNull(factory);
        assertTrue(factory instanceof DefaultKafkaConsumerFactory);
        assertTrue(factory.getConfigurationProperties().isEmpty(), "Consumer properties should be empty as per mock setup.");
    }
/*

    @Test
    @SuppressWarnings("unchecked")
    void kafkaListenerContainerFactory_shouldConfigureCorrectly_autoStartTrue() {
        // Arrange
        ConsumerFactory<String, String> mockConsumerFactory = mock(ConsumerFactory.class);
        ReflectionTestUtils.setField(kafkaConsumerConfiguration, "regularAutoStart", true);

        // Act
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                kafkaConsumerConfiguration.kafkaListenerContainerFactory(mockConsumerFactory);

        // Assert
        assertNotNull(factory);
        assertEquals(mockConsumerFactory, factory.getConsumerFactory());
        assertTrue(factory.isAutoStartup()); // As per subtask instruction
        assertEquals(5, factory.getContainerProperties().getConcurrency()); // As per subtask instruction
        assertNotNull(factory.getContainerProperties().getRecordFilterStrategy()); // As per subtask instruction

        // Test record filter strategy
        ConsumerRecord<String, String> filteredRecord = new ConsumerRecord<>("topic", 0, 0L, "key", "testValue");
        ConsumerRecord<String, String> nonFilteredRecord = new ConsumerRecord<>("topic", 0, 0L, "key", "Value");
        assertTrue(factory.getContainerProperties().getRecordFilterStrategy().filter(filteredRecord), "Record starting with 'test' should be filtered (return true)");
        assertFalse(factory.getContainerProperties().getRecordFilterStrategy().filter(nonFilteredRecord), "Record not starting with 'test' should not be filtered (return false)");
    }
    
    @Test
    @SuppressWarnings("unchecked")
    void kafkaListenerContainerFactory_shouldConfigureCorrectly_autoStartFalse() {
        // Arrange
        ConsumerFactory<String, String> mockConsumerFactory = mock(ConsumerFactory.class);
        ReflectionTestUtils.setField(kafkaConsumerConfiguration, "regularAutoStart", false);

        // Act
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                kafkaConsumerConfiguration.kafkaListenerContainerFactory(mockConsumerFactory);

        // Assert
        assertNotNull(factory);
        assertEquals(mockConsumerFactory, factory.getConsumerFactory());
        assertFalse(factory.isAutoStartup()); // As per subtask instruction
    }
*/


    @Test
    @SuppressWarnings("unchecked")
    void kafkaReceiver_shouldReturnConfiguredKafkaReceiver() {
        // Arrange
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put("bootstrap.servers", "localhost:9092"); 
        consumerProps.put("group.id", "test-group-reactive");

        when(mockKafkaProperties.getConsumer()).thenReturn(mockKafkaConsumerProperties);
        when(mockKafkaConsumerProperties.buildProperties(null)).thenReturn(consumerProps);
        
        // Mocking static KafkaReceiver.create method
        try (MockedStatic<KafkaReceiver> mockedStaticKafkaReceiver = Mockito.mockStatic(KafkaReceiver.class)) {
            ArgumentCaptor<ReceiverOptions> optionsCaptor = ArgumentCaptor.forClass(ReceiverOptions.class);
            KafkaReceiver<Integer, String> mockReceiver = mock(KafkaReceiver.class);
            
            // Ensure that when KafkaReceiver.create is called with any ReceiverOptions, our mockReceiver is returned.
            mockedStaticKafkaReceiver.when(() -> KafkaReceiver.create(optionsCaptor.capture())).thenReturn(mockReceiver);

            // Act
            KafkaReceiver<Integer, String> actualReceiver = kafkaConsumerConfiguration.kafkaReceiver(mockKafkaProperties);

            // Assert
            assertNotNull(actualReceiver);
            assertSame(mockReceiver, actualReceiver, "The mockReceiver should be returned by the factory method");

            // Verify options
            ReceiverOptions<?, ?> capturedOptions = optionsCaptor.getValue();
            assertNotNull(capturedOptions, "ReceiverOptions should not be null");
            assertEquals(Collections.singleton("my-topic"), capturedOptions.subscriptionTopics());
            assertEquals("localhost:9092", capturedOptions.consumerProperties().get("bootstrap.servers"));
            assertEquals("test-group-reactive", capturedOptions.consumerProperties().get("group.id"));
        }
    }
}
