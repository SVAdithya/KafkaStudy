package com.kafka.learn.kafkastudy.controller;

import com.kafka.learn.kafkastudy.config.KafkaConsumerConfiguration;
import com.kafka.learn.kafkastudy.controller.model.AppService;
import com.kafka.learn.kafkastudy.health.AppHealthIndicator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@AllArgsConstructor
public class HealthController {
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    private AppHealthIndicator appHealthIndicator;
    @GetMapping("/health/{service}")
    public String healthCheck(@PathVariable AppService service){
        logger.info("Start time : {}", ZonedDateTime.now());
        String s = appHealthIndicator.health().toString();
        logger.info("Health update : {}, {}",ZonedDateTime.now(), s);
        return s;
    }
}
