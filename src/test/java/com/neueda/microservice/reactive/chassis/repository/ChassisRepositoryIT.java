package com.neueda.microservice.reactive.chassis.repository;


import com.neueda.microservice.reactive.chassis.PostgresTestContainer;
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
class ChassisRepositoryIT extends PostgresTestContainer {

    @Autowired
    private ChassisRepository chassisRepository;

    @BeforeEach
    void setUp(@Autowired DatabaseClient database) {
        Hooks.onOperatorDebug();
        cleanChassisEntityTable(database);
    }

    @Test
    void shouldReadsAllChassisEntities() {
        // given
        insertChassisEntities(
                ChassisEntity.builder()
                        .name("find_all_chassis test")
                        .build());

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
