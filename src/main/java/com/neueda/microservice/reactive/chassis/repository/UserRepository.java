package com.neueda.microservice.reactive.chassis.repository;

import com.neueda.microservice.reactive.chassis.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,Long> {
}
