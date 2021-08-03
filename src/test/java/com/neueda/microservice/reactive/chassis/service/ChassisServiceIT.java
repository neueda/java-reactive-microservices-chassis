package com.neueda.microservice.reactive.chassis.service;

import com.neueda.microservice.reactive.chassis.PostgresTestContainer;
import com.neueda.microservice.reactive.chassis.model.Chassis;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    static void init(
            @Value("db/delete_all_chassis_entity.sql") Resource script) {

        executeSqlScript(script);
    }

    @BeforeEach
    void setUp() {
        Hooks.onOperatorDebug();
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
