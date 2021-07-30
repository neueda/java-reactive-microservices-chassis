package com.neueda.microservice.reactive.chassis;

import com.neueda.microservice.reactive.chassis.model.Chassis;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;

public abstract class PostgresTestContainer {

    @DynamicPropertySource
    static void setDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.liquibase.enabled", () -> "true");
    }

    public static void cleanChassisEntityTable(DatabaseClient database) {
        List.of("DELETE FROM chassis_entity;")
                .forEach(it -> database.sql(it)
                        .fetch()
                        .rowsUpdated()
                        .as(StepVerifier::create)
                        .expectNextCount(1)
                        .verifyComplete());
    }

    public static void insertItemInChassisEntityTable(DatabaseClient database, Chassis... chassis) {
        var statements = Stream.of(chassis).map(
                        it -> format("INSERT INTO chassis_entity(name, description) VALUES ('%s', '%s');",
                                it.name(), it.description()));

        statements.forEach(it -> database.sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }
}
