package com.kafka.learn.kafkastudy.listener;

import com.kafka.learn.kafkastudy.repository.SuccessMessageRepository;
import com.kafka.learn.kafkastudy.repository.dto.SuccessMessage;
import com.kafka.learn.kafkastudy.util.HeaderUtil;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverRecord;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReactiveKafkaListenerTest {

    @Mock
    private KafkaReceiver<Integer, String> mockKafkaReceiver;

    @Mock
    private HeaderUtil mockHeaderUtil;

    @Mock
    private SuccessMessageRepository mockSuccessMessageRepository;

    @InjectMocks
    private ReactiveKafkaListener reactiveKafkaListener;

    @Test
    @SuppressWarnings("unchecked") // For the mock(ReceiverRecord.class)
    void consumeMessages_processesAndAcksMessages() {
        // 1. Arrange
        // Mock ReceiverRecord 1
        ReceiverRecord<Integer, String> record1 = mock(ReceiverRecord.class);
        ReceiverOffset offset1 = mock(ReceiverOffset.class);
        Headers headers1 = new RecordHeaders(); // Empty headers for simplicity
        // when(record1.key()).thenReturn(1); // Unnecessary stubbing
        when(record1.value()).thenReturn("message1_content");
        when(record1.headers()).thenReturn(headers1);
        when(record1.offset()).thenReturn(100L);
        when(record1.receiverOffset()).thenReturn(offset1);

        // Mock ReceiverRecord 2
        ReceiverRecord<Integer, String> record2 = mock(ReceiverRecord.class);
        ReceiverOffset offset2 = mock(ReceiverOffset.class);
        Headers headers2 = new RecordHeaders().add("X-Test-Header", "Value".getBytes());
        // when(record2.key()).thenReturn(2); // Unnecessary stubbing
        when(record2.value()).thenReturn("message2_content");
        when(record2.headers()).thenReturn(headers2);
        when(record2.offset()).thenReturn(101L);
        when(record2.receiverOffset()).thenReturn(offset2);
        
        // Mock KafkaReceiver behavior to emit the two records
        when(mockKafkaReceiver.receive()).thenReturn(Flux.just(record1, record2));

        // Mock HeaderUtil behavior
        when(mockHeaderUtil.headersMapToString(headers1)).thenReturn("headers_string_1");
        when(mockHeaderUtil.headersMapToString(headers2)).thenReturn("headers_string_2");

        // Mock Repository behavior - return the saved message itself wrapped in Mono
        // ArgumentCaptor is used later to verify the actual saved object
        when(mockSuccessMessageRepository.save(any(SuccessMessage.class))).thenAnswer(invocation -> {
            SuccessMessage msg = invocation.getArgument(0);
            return Mono.just(msg);
        });
        
        // 2. Act
        // consumeMessages is called by run() in the CommandLineRunner implementation
        reactiveKafkaListener.run(); // This will internally call consumeMessages()

        // 3. Assert / Verify
        verify(mockKafkaReceiver).receive(); // Verify receive() was called

        // Verify interactions for record1
        verify(mockHeaderUtil).headersMapToString(headers1);
        verify(offset1).acknowledge();

        // Verify interactions for record2
        verify(mockHeaderUtil).headersMapToString(headers2);
        verify(offset2).acknowledge();
        
        // Capture all arguments to successMessageRepository.save()
        ArgumentCaptor<SuccessMessage> successMessageCaptor = ArgumentCaptor.forClass(SuccessMessage.class);
        verify(mockSuccessMessageRepository, times(2)).save(successMessageCaptor.capture());
        
        // Verify details for the first saved message
        SuccessMessage savedMessage1 = successMessageCaptor.getAllValues().get(0);
        assertEquals("message1_content", savedMessage1.body());
        assertEquals("headers_string_1", savedMessage1.headers());
        assertEquals(100L, savedMessage1.offset());
        assertNotNull(savedMessage1.id());
        assertNotNull(savedMessage1.time());

        // Verify details for the second saved message
        SuccessMessage savedMessage2 = successMessageCaptor.getAllValues().get(1);
        assertEquals("message2_content", savedMessage2.body());
        assertEquals("headers_string_2", savedMessage2.headers());
        assertEquals(101L, savedMessage2.offset());
        assertNotNull(savedMessage2.id());
        assertNotNull(savedMessage2.time());
    }

    @Test
    void consumeMessages_handlesEmptyFlux() {
        // Given
        when(mockKafkaReceiver.receive()).thenReturn(Flux.empty());

        // When
        reactiveKafkaListener.run();

        // Then
        verify(mockKafkaReceiver).receive();
        verifyNoInteractions(mockHeaderUtil);
        verifyNoInteractions(mockSuccessMessageRepository);
    }

    @Test
    void consumeMessages_handlesErrorInFlux() {
        // Given
        ReceiverRecord<Integer, String> record1 = mock(ReceiverRecord.class);
        ReceiverOffset offset1 = mock(ReceiverOffset.class);
        Headers headers1 = new RecordHeaders();
        when(record1.value()).thenReturn("message1_content");
        when(record1.headers()).thenReturn(headers1);
        when(record1.offset()).thenReturn(100L);
        when(record1.receiverOffset()).thenReturn(offset1);
        
        when(mockKafkaReceiver.receive()).thenReturn(Flux.just(record1)
                .concatWith(Flux.error(new RuntimeException("Kafka flux error!"))));

        when(mockHeaderUtil.headersMapToString(any())).thenReturn("headers_string");
        when(mockSuccessMessageRepository.save(any(SuccessMessage.class))).thenReturn(Mono.empty());

        // When
        reactiveKafkaListener.run(); // Call run which calls consumeMessages

        // Then
        // Verify processing for the first record still happens
        verify(mockKafkaReceiver).receive();
        verify(mockHeaderUtil).headersMapToString(record1.headers());
        ArgumentCaptor<SuccessMessage> successMessageCaptor = ArgumentCaptor.forClass(SuccessMessage.class);
        verify(mockSuccessMessageRepository).save(successMessageCaptor.capture());
        assertEquals("message1_content", successMessageCaptor.getValue().body());
        verify(offset1).acknowledge();
        // The error in Flux should be logged by Reactor Kafka internals or the subscribe block's error handler
        // but the test ensures that the processing of valid messages before error still occurs.
    }
}
