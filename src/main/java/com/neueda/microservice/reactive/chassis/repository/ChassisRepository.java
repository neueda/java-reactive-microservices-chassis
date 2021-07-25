package com.neueda.microservice.reactive.chassis.repository;

import com.neueda.microservice.reactive.chassis.domain.ChassisEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChassisRepository extends ReactiveCrudRepository<ChassisEntity, Long> {



}
