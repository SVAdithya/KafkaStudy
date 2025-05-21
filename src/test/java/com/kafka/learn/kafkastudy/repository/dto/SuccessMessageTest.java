package com.kafka.learn.kafkastudy.repository.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SuccessMessageTest {

    @Test
    void successMessage_shouldHoldDataCorrectly() {
        String id = "test-id";
        String headers = "test-headers";
        String body = "test-body";
        Long offset = 123L;
        String time = "test-time";

        SuccessMessage message = new SuccessMessage(id, headers, body, offset, time);

        assertEquals(id, message.id());
        assertEquals(headers, message.headers());
        assertEquals(body, message.body());
        assertEquals(offset, message.offset());
        assertEquals(time, message.time());
    }

    @Test
    void successMessage_testEqualsAndHashCode() {
        SuccessMessage message1 = new SuccessMessage("id1", "h1", "b1", 1L, "t1");
        SuccessMessage message2 = new SuccessMessage("id1", "h1", "b1", 1L, "t1");
        SuccessMessage message3 = new SuccessMessage("id2", "h2", "b2", 2L, "t2");
        SuccessMessage message4 = new SuccessMessage("id1", "h1", "b1", 1L, "DIFFERENT_TIME");


        assertEquals(message1, message2, "Messages with same data should be equal");
        assertEquals(message1.hashCode(), message2.hashCode(), "Hashcodes for equal messages should be same");
        assertNotEquals(message1, message3, "Messages with different data should not be equal");
        assertNotEquals(message1.hashCode(), message3.hashCode(), "Hashcodes for different messages should ideally be different");
        assertNotEquals(message1, message4, "Messages with different time should not be equal");
    }

    @Test
    void successMessage_testToString() {
        SuccessMessage message = new SuccessMessage("id", "h", "b", 1L, "t");
        // Standard record toString format
        String expectedString = "SuccessMessage[id=id, headers=h, body=b, offset=1, time=t]";
        assertEquals(expectedString, message.toString());
    }
}
