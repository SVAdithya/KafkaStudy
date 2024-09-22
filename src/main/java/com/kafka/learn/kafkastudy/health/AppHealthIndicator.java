package com.kafka.learn.kafkastudy.health;

import com.kafka.learn.kafkastudy.controller.model.AppService;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@AllArgsConstructor
@Component
public class AppHealthIndicator implements HealthIndicator {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    //private final AdminClient adminClient;

    @Override
    public Health health() {
        try {
            return Health.up()
                    .withDetail(AppService.REGULAR_KAFKA.name(), regularKafkaHealth())
                    //.withDetail(AppService.REACTIVE_KAFKA.name(), reactiveKafkaHealth())
                    .withDetail(AppService.MONGO.name(), mongoHealth())
                    .build();
        }catch (Exception e) {
            return Health.down().build();
        }
    }

    private String regularKafkaHealth() {
        try {
            //kafkaAdminClient.describeCluster().nodes().get();
            MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("regularKafkaListener");

            if (listenerContainer != null && listenerContainer.isRunning())
                return Status.UP.toString();
            return Status.DOWN.toString();
        } catch (Exception e) {
            return Status.DOWN.toString();
        }
    }

/* //TODO:
    private String reactiveKafkaHealth() {
        try {
            ListTopicsResult result = adminClient.listTopics();
            return result.listings().get().size() > 0 ? Status.UP.toString() : Status.DOWN.toString(); // Wait for the result
        } catch (InterruptedException | ExecutionException e) {
            return Status.DOWN.toString(); // Topic is not accessible
        }
    }
*/

    private String mongoHealth() {
        try {
            Mono<Health> h = reactiveMongoTemplate.executeCommand("{ ping: 1 }")
                    .flatMap(result -> Mono.just(Health.up().withDetail(AppService.MONGO.name(), Status.UP).build()))
                    .onErrorResume(ex -> Mono.just(Health.down().withDetail(AppService.MONGO.name(), Status.DOWN).build()));
            Optional<Health> health = h.blockOptional(Duration.ofSeconds(5));
            return health.isPresent() ? health.get().getStatus().toString() : Status.DOWN.toString();
        } catch (Exception e) {
            return "DOWN";
        }
    }
}
