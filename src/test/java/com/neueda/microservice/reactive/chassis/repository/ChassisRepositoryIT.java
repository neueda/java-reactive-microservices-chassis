package com.neueda.microservice.reactive.chassis.repository;


import com.neueda.microservice.reactive.chassis.entity.ChassisEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.util.Arrays;

@DataR2dbcTest
public class ChassisRepositoryIT {

    @Autowired
    private ChassisRepository chassisRepository;

    @Autowired
    private DatabaseClient database;

    @BeforeEach
    void setup() {
        Hooks.onOperatorDebug();

//        var statements = Arrays.asList(
//                "DROP TABLE IF EXISTS chassis_entity;",
//                "CREATE TABLE chassis_entity ( id IDENTITY PRIMARY KEY, name VARCHAR(50) NOT NULL, description VARCHAR(255));");
//
//        statements.forEach(it -> database.sql(it)
//                .fetch()
//                .rowsUpdated()
//                .as(StepVerifier::create)
//                .expectNextCount(1)
//                .verifyComplete());
    }

    @Test
    void shouldReadsAllChassisEntities() {
        // given
        var chassisItem = ChassisEntity.builder()
                .name("find_all_chassis test")
                .build();
        insertChassisEntities(chassisItem);

        // when
        chassisRepository.findAll()
                .as(StepVerifier::create)
                // then
                .expectNextCount(1)
                .verifyComplete();
    }

    private void insertChassisEntities(ChassisEntity... chassisEntities) {
        chassisRepository.saveAll(Arrays.asList(chassisEntities))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
