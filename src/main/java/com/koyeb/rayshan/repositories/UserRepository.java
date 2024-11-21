package com.koyeb.rayshan.repositories;

import com.koyeb.rayshan.entities.UsersEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<UsersEntity, Long> {
}
