package com.koyeb.rayshan.services;

import com.koyeb.rayshan.configs.AppConfig;
import com.koyeb.rayshan.entities.ExpenseEntity;
import com.koyeb.rayshan.entities.UsersEntity;
import com.koyeb.rayshan.repositories.ExpenseRepository;
import com.koyeb.rayshan.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class ExpenseService {
    private AppConfig appConfig;
    private UserRepository userRepository;
    private ExpenseRepository expenseRepository;

    public ExpenseService(AppConfig appConfig, UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.appConfig = appConfig;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

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
        log.info("Saying goodbye: {}", appConfig.getNeonDbUsername());
        return userRepository.findById(12l).map(UsersEntity::getName).map(name -> "goodbye:" + name);
        //return Mono.just("goodbye: " + input);
    }

    public Mono<Page<ExpenseEntity>> findAll(Pageable pageable) {
        log.info("Reading expense records.");
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        Mono<Long> countMono = expenseRepository.count();
        Flux<ExpenseEntity> dataFlux = expenseRepository.findAllWithPagination(limit, offset);

        return countMono.flatMap(total ->
                dataFlux.collectList()
                        .map(data -> new PageImpl<>(data, pageable, total))
        );
    }
}
