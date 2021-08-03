package com.neueda.microservice.reactive.repository;

import com.neueda.microservice.reactive.entity.ChassisEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChassisRepository extends
        ReactiveCrudRepository<ChassisEntity, Long>,
        ReactiveQueryByExampleExecutor<ChassisEntity> {
}
