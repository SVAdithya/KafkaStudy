package com.kafka.learn.kafkastudy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class KafkaStudyApplicationTest {

    @Test
    void instantiationTest() {
        // Test that the application class can be instantiated
        KafkaStudyApplication application = null;
        try {
            application = new KafkaStudyApplication();
        } catch (Exception e) {
            fail("Instantiation of KafkaStudyApplication failed: " + e.getMessage());
        }
        assertNotNull(application, "KafkaStudyApplication instance should not be null");
    }

    @Test
    void mainMethodTest_shouldCallSpringApplicationRun() {
        // Mock SpringApplication.run to avoid starting the full context
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            
            mockedSpringApplication.when(() -> SpringApplication.run(eq(KafkaStudyApplication.class), any(String[].class)))
                .thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

            // Call the main method
            KafkaStudyApplication.main(new String[]{});

            // Verify that SpringApplication.run was called with the correct arguments
            mockedSpringApplication.verify(() -> SpringApplication.run(KafkaStudyApplication.class, new String[]{}));
        }
    }
}
