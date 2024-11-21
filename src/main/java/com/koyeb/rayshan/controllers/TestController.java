package com.koyeb.rayshan.controllers;

import com.koyeb.rayshan.services.TestService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
@Log4j2
public class TestController {
    private TestService service;

    public TestController(TestService testService) {
        this.service = testService;
    }

    @GetMapping("/")
    public Mono<String> hello() {
        log.info("Request received >>>>>>");
        return service.getTestResult();
    }

    @GetMapping("/test2")
    public Mono<String> test2() {
        log.info("Request test2 received >>>>>>");
        log.info("NEON_DB_USERNAME: {}", System.getenv("NEON_DB_USERNAME"));
        return service.getTestResult();
    }
}
