package com.koyeb.rayshan.repositories;

import com.koyeb.rayshan.entities.ExpenseEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExpenseRepository extends ReactiveCrudRepository<ExpenseEntity, Long> {
    @Query("SELECT * FROM user_expense_data LIMIT :limit OFFSET :offset")
    Flux<ExpenseEntity> findAllWithPagination(int limit, int offset);

    @Query("SELECT COUNT(*) FROM user_expense_data")
    Mono<Long> count();
}

