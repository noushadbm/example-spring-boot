package com.koyeb.rayshan.services;

import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class TestService {
    public Mono<String> getTestResult() {
        log.info("Request received in service.");
        return Mono.zip(task1(), task2(), (t1, t2) -> {
            log.info("Returning final result.");
            return String.format("Result: %s, %s", t1, t2);
        }).flatMap(this::sayGoodBye);
    }

    private Mono<String> task1() {
        log.info("Task1 called");
        return Mono.just("Value1");
    }

    private Mono<String> task2() {
        log.info("Task2 called");
        return Mono.just("Value2");
    }

    private Mono<String> sayGoodBye(String input) {
        log.info("Saying goodbye");
        return Mono.just("goodbye: " + input);
    }
}
