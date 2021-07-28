package com.neueda.microservice.reactive.chassis.repository;


import com.neueda.microservice.reactive.chassis.entity.ChassisEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.util.Arrays;

@SpringBootTest
public class ChassisRepositoryTests {

    @Autowired
    private ChassisRepository chassisRepository;

    @Autowired
    private DatabaseClient database;

    @BeforeEach
    void setUp() {
        Hooks.onOperatorDebug();

        var statements = Arrays.asList(
                "DROP TABLE IF EXISTS chassis_entity;",
                "CREATE TABLE chassis_entity ( id IDENTITY PRIMARY KEY, name VARCHAR(50) NOT NULL, description VARCHAR(255));");

        statements.forEach(it -> database.sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }

    @Test
    void readsAllEntitiesCorrectly() {
        var chassisItem = new ChassisEntity();
        chassisItem.setName("find_all_chassis test");

        insertChassisItem(chassisItem);

        chassisRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    private void insertChassisItem(ChassisEntity... chassisEntities) {

        chassisRepository.saveAll(Arrays.asList(chassisEntities))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
