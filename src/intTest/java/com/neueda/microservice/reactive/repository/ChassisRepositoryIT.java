package com.neueda.microservice.reactive.repository;


import com.neueda.microservice.reactive.PostgresTestContainer;
import com.neueda.microservice.reactive.entity.ChassisEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.util.Arrays;

@DataR2dbcTest
class ChassisRepositoryIT extends PostgresTestContainer {

    @Autowired
    private ChassisRepository chassisRepository;

    @BeforeAll
    static void init(
            @Value("classpath:db/delete_all_chassis_entity.sql") Resource script) {

        executeScript(script);
    }

    @BeforeEach
    void setup() {
        Hooks.onOperatorDebug();
    }

    @Test
    @DisplayName("Should get all chassis entities")
    void shouldGetAllChassisEntities() {
        // given
        insertChassisEntities(
                new ChassisEntity().setName("list_all_chassis test"));

        // when
        chassisRepository.findAll()
                .as(StepVerifier::create)
                // then
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    private void insertChassisEntities(ChassisEntity... chassisEntities) {
        // when
        chassisRepository.saveAll(Arrays.asList(chassisEntities))
                .as(StepVerifier::create)
                // then
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }
}
