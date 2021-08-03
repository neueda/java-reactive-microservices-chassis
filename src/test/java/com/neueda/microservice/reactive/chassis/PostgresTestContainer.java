package com.neueda.microservice.reactive.chassis;

import io.r2dbc.spi.ConnectionFactories;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.PostgreSQLR2DBCDatabaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Testcontainers
public abstract class PostgresTestContainer {
    private static final DockerImageName imageName =
            DockerImageName.parse("postgres").withTag("13.3-alpine");

    @Container
    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>(imageName).withReuse(true);

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        final String postgresUrl =
                format("postgresql://%s:%d/test", container.getHost(), container.getFirstMappedPort());

        // Liquibase DataSource
        registry.add("spring.liquibase.enabled", () -> "true");
        registry.add("spring.liquibase.url", () -> format("jdbc:%s?loggerLevel=DEBUG", postgresUrl));
        registry.add("spring.liquibase.user", () -> "test");
        registry.add("spring.liquibase.password", () -> "test");

        // R2DBC DataSource
        registry.add("spring.r2dbc.url", () -> format("r2dbc:pool:%s", postgresUrl));
        registry.add("spring.r2dbc.username", () -> "test");
        registry.add("spring.r2dbc.password", () -> "test");
    }

    public static void executeSqlScript(Resource script) {
        var connectionFactory =
                ConnectionFactories.get(PostgreSQLR2DBCDatabaseContainer.getOptions(container));

        Mono.from(connectionFactory.create())
                .flatMap(c -> ScriptUtils.executeSqlScript(c, script))
                .block();
    }
}
