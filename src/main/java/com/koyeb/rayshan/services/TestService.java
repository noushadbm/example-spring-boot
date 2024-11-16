package com.koyeb.rayshan.services;

import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class TestService {
    public Mono<String> getTestResult() {
        log.info("Request received in service.");
        return Mono.just(String.format("Hello world second time!"));
    }
}
