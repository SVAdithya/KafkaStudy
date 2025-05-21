package com.kafka.learn.kafkastudy.listener;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegularKafkaListenerTest {
    @Test
    void testListenerInstantiation() {
        RegularKafkaListener listener = new RegularKafkaListener();
        assertNotNull(listener, "Listener should be instantiated");
    }
}
