package com.kafka.learn.kafkastudy.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionCodeTest {
    @Test
    void testEnumValues() {
        assertEquals("MONGO_DB_EXCEPTION :", ExceptionCode.E5001_M.getValue());
        assertEquals("KAFKA_EXCEPTION :", ExceptionCode.E5002_K.getValue());
        assertEquals("APPLICATION_EXCEPTION :", ExceptionCode.E5003_GEN.getValue());
        assertEquals("DATABASE_CONNECTION_EXCEPTION :", ExceptionCode.E5004_DB.getValue());
    }

    @Test
    void testValueOf() {
        assertEquals(ExceptionCode.E5001_M, ExceptionCode.valueOf("E5001_M"));
        assertEquals(ExceptionCode.E5002_K, ExceptionCode.valueOf("E5002_K"));
        assertEquals(ExceptionCode.E5003_GEN, ExceptionCode.valueOf("E5003_GEN"));
        assertEquals(ExceptionCode.E5004_DB, ExceptionCode.valueOf("E5004_DB"));
    }
}
