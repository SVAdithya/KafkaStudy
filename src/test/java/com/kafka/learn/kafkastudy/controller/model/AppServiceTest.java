package com.kafka.learn.kafkastudy.controller.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppServiceTest {

    @Test
    void testEnumValues() {
        // Call values() to ensure all enum constants are covered
        AppService[] services = AppService.values();
        assertTrue(services.length > 0, "Should have at least one AppService value");

        // Optionally, verify specific values if needed
        assertEquals(AppService.MONGO, AppService.valueOf("MONGO"));
        assertEquals(AppService.REGULAR_KAFKA, AppService.valueOf("REGULAR_KAFKA"));
        assertEquals(AppService.REACTIVE_KAFKA, AppService.valueOf("REACTIVE_KAFKA"));
        assertEquals(AppService.APP, AppService.valueOf("APP"));
        assertEquals(AppService.OTHERS, AppService.valueOf("OTHERS"));
    }

    @Test
    void testValueOf() {
        // Test that valueOf returns the correct enum constants
        assertSame(AppService.MONGO, AppService.valueOf("MONGO"));
        assertSame(AppService.REGULAR_KAFKA, AppService.valueOf("REGULAR_KAFKA"));
        assertSame(AppService.REACTIVE_KAFKA, AppService.valueOf("REACTIVE_KAFKA"));
        assertSame(AppService.APP, AppService.valueOf("APP"));
        assertSame(AppService.OTHERS, AppService.valueOf("OTHERS"));
    }

    @Test
    void testToString() {
        // Enums toString() method returns the name of the enum constant
        assertEquals("MONGO", AppService.MONGO.toString());
        assertEquals("REGULAR_KAFKA", AppService.REGULAR_KAFKA.toString());
        assertEquals("REACTIVE_KAFKA", AppService.REACTIVE_KAFKA.toString());
        assertEquals("APP", AppService.APP.toString());
        assertEquals("OTHERS", AppService.OTHERS.toString());
    }
}
