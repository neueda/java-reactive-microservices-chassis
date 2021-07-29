package com.neueda.microservice.reactive.chassis.service;

import com.neueda.microservice.reactive.chassis.model.Chassis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(ChassisService.class)
public class ChassisServiceIT {

    @Autowired
    private ChassisService chassisService;

    @BeforeEach
    void setUp() {
        Hooks.onOperatorDebug();
    }

    @Test
    void shouldFindAChassisByPartialName() {
        // given
        insertAChassisEntity(
                new Chassis("partial name find test", "description text"));

        //when
        chassisService.searchChassisByName("find")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    private void insertAChassisEntity(Chassis chassis) {
        chassisService.addChassis(chassis)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
