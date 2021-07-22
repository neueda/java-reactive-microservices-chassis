package com.neueda.reactiveapitemplate.repository;

import com.neueda.reactiveapitemplate.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,Long> {
}
