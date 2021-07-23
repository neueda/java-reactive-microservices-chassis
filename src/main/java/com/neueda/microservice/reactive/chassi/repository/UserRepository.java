package com.neueda.microservice.reactive.chassi.repository;

import com.neueda.microservice.reactive.chassi.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,Long> {
}
