/*
package com.kafka.learn.kafkastudy.listener;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@TestPropertySource(properties = "regular.kafka.autostart=true")
@ExtendWith(OutputCaptureExtension.class)
public class RegularKafkaListenerIntegrationTest {

    @Container
    // @ServiceConnection // Not using this to follow the more explicit setup in the prompt
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        // It's good practice to ensure the container is running before trying to get its properties.
        // Testcontainers @Container annotation usually handles starting it before @DynamicPropertySource.
        if (!kafka.isRunning()) {
             kafka.start();
        }
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.regular.topic}")
    private String regularTopic;

    @BeforeAll
    static void setUpClass() {
        // kafka.start(); // Handled by @Container or @DynamicPropertySource
        // Create topics if necessary - good practice for Testcontainers
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        try (AdminClient admin = AdminClient.create(config)) {
            // Topic name is hardcoded in RegularKafkaListener's @KafkaListener annotation
            // and also in application-test.properties
            NewTopic newTopic = new NewTopic("test-regular-topic", 1, (short) 1);
            admin.createTopics(Collections.singleton(newTopic)).all().get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Log or handle topic creation failure if needed, though for tests often it's fine if it already exists
            System.err.println("Failed to create topic: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDownClass() {
        kafka.stop();
    }

    @Test
    void testConsumeMessage_logsMessageAndHeaders(CapturedOutput outputCapture) {
        String messagePayload = "test This is a regular Kafka message for logging";
        kafkaTemplate.send(regularTopic, messagePayload);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            String logs = outputCapture.toString();
            assertTrue(logs.contains("Regular message: " + messagePayload), "Log should contain the message payload");
            assertTrue(logs.contains("Headers: {}"), "Log should contain empty headers for this test message");
        });
    }

    @Test
    void testConsumeMessage_isFiltered(CapturedOutput outputCapture) {
        String filteredMessagePayload = "FilterThis Regular message"; // Does not start with "test"
        kafkaTemplate.send(regularTopic, filteredMessagePayload);

        // Give Kafka some time to process/filter
        try {
            Thread.sleep(2000); // Using simple sleep as an alternative for waiting for *absence* of logs
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String logs = outputCapture.toString();
        assertFalse(logs.contains("Regular message: " + filteredMessagePayload), "Log should NOT contain the filtered message payload");
        // The listener itself logs "Record filtered: filterThisMessage"
        // We need to make sure that the listener's filter logic is being tested.
        // The configuration has: factory.setRecordFilterStrategy(record -> record.value().startsWith("test"));
        // So, the listener itself doesn't log "Record filtered". The filter in the factory does the filtering.
        // The test should ensure the message is NOT processed by the listener's logMessage method.
        // If there were a specific log for filtered records from the listener itself, we'd check for that.
        // For now, absence of the processing log is the key.
    }
}
*/
