package com.koyeb.rayshan.repositories;

import com.koyeb.rayshan.entities.UsersEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UsersEntity, Long> {
    Mono<UsersEntity> findByName(String userName);
}
