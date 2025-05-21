package com.kafka.learn.kafkastudy.controller;

import com.kafka.learn.kafkastudy.health.AppHealthIndicator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppHealthIndicator appHealthIndicator;

    @Test
    void healthCheck_shouldReturnHealthStatus() throws Exception {
        // Given
        // The Health.toString() method returns a string like "UP {details...}" or "DOWN {details...}"
        // We are interested in the status part, so "UP" or "DOWN".
        String expectedStatusSubstring = "UP";
        Health health = Health.up().withDetail("detailKey", "detailValue").build();
        when(appHealthIndicator.health()).thenReturn(health);

        // When & Then
        mockMvc.perform(get("/health/MONGO")) // Using MONGO from AppService enum
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(expectedStatusSubstring)))
               .andExpect(content().string(containsString("detailKey")))
               .andExpect(content().string(containsString("detailValue")));
    }

    @Test
    void healthCheck_shouldReturnDownStatus() throws Exception {
        // Given
        String expectedStatusSubstring = "DOWN";
        Health health = Health.down().withDetail("error", "MongoDB connection failed").build();
        when(appHealthIndicator.health()).thenReturn(health);

        // When & Then
        mockMvc.perform(get("/health/MONGO"))
                .andExpect(status().isOk()) // The controller itself returns 200 OK, the health status is in the body
                .andExpect(content().string(containsString(expectedStatusSubstring)))
                .andExpect(content().string(containsString("error")))
                .andExpect(content().string(containsString("MongoDB connection failed")));
    }
}
