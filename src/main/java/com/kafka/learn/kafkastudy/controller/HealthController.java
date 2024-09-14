package com.kafka.learn.kafkastudy.controller;

import com.kafka.learn.kafkastudy.controller.model.AppService;
import com.kafka.learn.kafkastudy.health.AppHealthIndicator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class HealthController {
    private AppHealthIndicator appHealthIndicator;
    @GetMapping("/health/{service}")
    public ResponseBody healthCheck(@PathVariable AppService service){
        appHealthIndicator.getHealth(Boolean.TRUE);
        return null;
    }
}
