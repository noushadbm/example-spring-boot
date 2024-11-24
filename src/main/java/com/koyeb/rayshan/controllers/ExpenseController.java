package com.koyeb.rayshan.controllers;

import com.koyeb.rayshan.entities.ExpenseEntity;
import com.koyeb.rayshan.models.ApiResponse;
import com.koyeb.rayshan.models.AuthData;
import com.koyeb.rayshan.models.LoginRequest;
import com.koyeb.rayshan.services.AuthService;
import com.koyeb.rayshan.services.ExpenseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Log4j2
public class ExpenseController {
    private final ExpenseService service;
    private final AuthService authService;

    public ExpenseController(ExpenseService expenseService, AuthService authService) {
        this.service = expenseService;
        this.authService = authService;
    }

    @GetMapping("/test")
    public Mono<String> test2() {
        log.info("===> Test request");
        return Mono.just("pass");
    }

    @GetMapping("/list")
    public Mono<Page<ExpenseEntity>> getExpensesPaged() {
        log.info("getExpensesPaged >>>>>>");
        Pageable pageable = Pageable.ofSize(100);
        return service.findAll(pageable);
    }

    @PostMapping("/authenticate")
    public Mono<ApiResponse<AuthData>> login(
            @RequestBody LoginRequest loginRequest
            ) {
        log.info("Login request received. Username: {}", loginRequest.getUserName());
        return authService.authenticate(loginRequest).map(authData -> {
            ApiResponse<AuthData> response = new ApiResponse<>();
            response.setStatusCode(200L);
            response.setStatusMsg("SUCCESS");
            response.setData(authData);

            log.info("Returning success login response.");
            return response;
        }).onErrorResume(e -> {
            log.error("Login failed for user: {}. Error: {}", loginRequest.getUserName(), e.getMessage());
            e.printStackTrace();
            ApiResponse<AuthData> response = new ApiResponse<>();
            response.setStatusCode(401L);
            response.setStatusMsg("FAILED");
            response.setError(e.getMessage());
            return Mono.just(response);
        });
    }
}
