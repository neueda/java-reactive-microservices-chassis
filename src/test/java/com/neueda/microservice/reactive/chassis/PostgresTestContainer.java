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
    private static final DockerImageName postgresImage =
            DockerImageName.parse("postgres").withTag("13.3-alpine");

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(postgresImage).withReuse(true);

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        String databaseName = postgresContainer.getDatabaseName();
        String postgresUrl = format("postgresql://%s:%d/%s",
                postgresContainer.getHost(), postgresContainer.getFirstMappedPort(), databaseName);

        // Liquibase DataSource
        registry.add("spring.liquibase.url", () -> "jdbc:" + postgresUrl);
        registry.add("spring.liquibase.user", () -> databaseName);
        registry.add("spring.liquibase.password", () -> databaseName);

        // R2DBC DataSource
        registry.add("spring.r2dbc.url", () -> "r2dbc:pool:" + postgresUrl);
        registry.add("spring.r2dbc.username", () -> databaseName);
        registry.add("spring.r2dbc.password", () -> databaseName);
    }

    public static void executeSqlScript(Resource script) {
        var connectionFactory =
                ConnectionFactories.get(PostgreSQLR2DBCDatabaseContainer.getOptions(postgresContainer));

        Mono.from(connectionFactory.create())
                .flatMap(c -> ScriptUtils.executeSqlScript(c, script))
                .block();
    }
}
