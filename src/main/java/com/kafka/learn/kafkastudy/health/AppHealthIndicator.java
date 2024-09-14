package com.kafka.learn.kafkastudy.health;

import com.mongodb.reactivestreams.client.MongoClient;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AppHealthIndicator implements HealthIndicator {
    private final MongoClient mongoClient;
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    //private final AdminClient kafkaAdminClient;


    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetails(kafkaHealth().getDetails())
                .withDetails(mongoHealth().getDetails())
                .build();
    }

    private Health kafkaHealth() {
        try {
            //kafkaAdminClient.describeCluster().nodes().get();
            MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("listenerOne");

            if (listenerContainer != null && listenerContainer.isRunning())
                return Health.up().withDetail("RegularKafkaListener", "UP").build();
            return Health.down().withDetail("RegularKafkaListener", "DOWN").build();
        } catch (Exception e) {
            return Health.down().withDetail("Kafka", "Not Available").withException(e).build();
        }
    }

    private Health mongoHealth() {
        try {
            mongoClient.listDatabaseNames();
            return Health.up().withDetail("MongoDB", "Available").build();
        } catch (Exception e) {
            return Health.down().withDetail("MongoDB", "Not Available").withException(e).build();
        }
    }
}
