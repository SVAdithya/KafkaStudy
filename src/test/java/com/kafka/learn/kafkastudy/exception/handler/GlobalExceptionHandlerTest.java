package com.kafka.learn.kafkastudy.exception.handler;

import com.kafka.learn.kafkastudy.exception.ExceptionCode;
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.ServerAddress;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GlobalExceptionHandler.class, GlobalExceptionHandlerTest.TestController.class})
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    // Mock any dependencies that TestController might have if it were more complex.
    // For this setup, TestController is simple and self-contained.
    // If Kafka beans were involved in the controller path, they might need mocking:
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;


    @RestController
    static class TestController {
        @GetMapping("/test/global-exception")
        public void throwGlobalException() throws Exception {
            throw new Exception("Global error");
        }

        @GetMapping("/test/data-access-exception")
        public void throwDataAccessException() {
            throw new DataAccessResourceFailureException("DB error"); // A subclass of DataAccessException
        }

        @GetMapping("/test/kafka-exception")
        public void throwKafkaException() {
            throw new KafkaException("Kafka error");
        }

        @GetMapping("/test/mongo-exception")
        public void throwMongoException() {
            throw new MongoSocketOpenException("Mongo connection error", new ServerAddress("localhost", 27017), new Exception());
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // globalExceptionHandler will be automatically picked up by Spring's MockMvc context
        // when using @WebMvcTest and @ContextConfiguration pointing to it.
        // However, to specifically test it with a controller that throws exceptions,
        // we set it up with MockMvcBuilders for the TestController.
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                                 .setControllerAdvice(globalExceptionHandler)
                                 .build();
    }

    @Test
    void handleGlobalException() throws Exception {
        mockMvc.perform(get("/test/global-exception")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status", is(ExceptionCode.E5003_GEN.toString())))
                .andExpect(jsonPath("$.message", is(ExceptionCode.E5003_GEN.getValue() + "Global error")));
    }

    @Test
    void handleDataAccessException() throws Exception {
        mockMvc.perform(get("/test/data-access-exception")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status", is(ExceptionCode.E5004_DB.toString())))
                .andExpect(jsonPath("$.message", is(ExceptionCode.E5004_DB.getValue() + "DB error")));
    }

    @Test
    void handleKafkaException() throws Exception {
        mockMvc.perform(get("/test/kafka-exception")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status", is(ExceptionCode.E5002_K.toString())))
                .andExpect(jsonPath("$.message", is(ExceptionCode.E5002_K.getValue() + "Kafka error")));
    }

    @Test
    void handleMongoException() throws Exception {
        // For MongoException, the message in the response includes ex.getMessage()
        // MongoSocketOpenException's getMessage() is "Mongo connection error"
        mockMvc.perform(get("/test/mongo-exception")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status", is(ExceptionCode.E5001_M.toString())))
                .andExpect(jsonPath("$.message", is(ExceptionCode.E5001_M.getValue() + "Mongo connection error")));
    }
}
