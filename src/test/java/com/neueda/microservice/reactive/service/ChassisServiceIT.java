package com.neueda.microservice.reactive.service;

import com.neueda.microservice.reactive.PostgresTestContainer;
import com.neueda.microservice.reactive.exception.EntityNotFoundException;
import com.neueda.microservice.reactive.model.Chassis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(ChassisService.class)
class ChassisServiceIT extends PostgresTestContainer {

    @Autowired
    private ChassisService chassisService;

    @BeforeEach
    void setUp(
            @Value("db/delete_all_chassis_entity.sql") Resource script) {

        Hooks.onOperatorDebug();
        executeSqlScript(script);
    }

    @Test
    void shouldThrowNotFoundException() {
        // when
        chassisService.getChassisById(1L)
                .as(StepVerifier::create)
                // then
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    void shouldFindAChassisByPartialName() {
        // given
        insertAChassisEntity(
                new Chassis("partial name find test", "description text"));

        // when
        chassisService.searchChassisByNameContaining("find")
                .as(StepVerifier::create)
                // then
                .expectNextCount(1)
                .verifyComplete();
    }

    private void insertAChassisEntity(Chassis chassis) {
        chassisService.addChassis(chassis)
                .as(StepVerifier::create)
                // then
                .expectNextCount(1)
                .verifyComplete();
    }
}
