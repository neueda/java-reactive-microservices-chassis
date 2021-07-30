package com.neueda.microservice.reactive.chassis;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.core.publisher.Mono;

import java.util.Objects;

public abstract class PostgresTestContainer {

    private static ConnectionFactory connectionFactory;

    public static void setConnectionFactory(ConnectionFactory connectionFactory) {
        PostgresTestContainer.connectionFactory = connectionFactory;
    }

    public static void executeSqlScript(Resource script) {
        Objects.requireNonNull(connectionFactory,
                "Prior to call method executeSqlScript a ConnectionFactory must to be set");

        Mono.from(connectionFactory.create())
                .flatMap(c -> ScriptUtils.executeSqlScript(c, script))
                .block();
    }

    @DynamicPropertySource
    private static void setDatabaseProperties(DynamicPropertyRegistry registry) {
        // Liquibase
        registry.add("spring.liquibase.enabled", () -> "true");
        registry.add("spring.liquibase.url", () -> "jdbc:h2:file:./target/tmp/chassisdb");
        registry.add("spring.liquibase.user", () -> "sa");

        // r2dbc
        registry.add("spring.r2dbc.url", () -> "r2dbc:pool:h2:file:///./target/tmp/chassisdb?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false");
        registry.add("spring.r2dbc.user", () -> "sa");
    }
}
