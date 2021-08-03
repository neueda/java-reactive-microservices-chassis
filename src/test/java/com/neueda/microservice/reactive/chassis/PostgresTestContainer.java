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

import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;

@Testcontainers
public abstract class PostgresTestContainer {
    
    private static final DockerImageName postgresImage =
            DockerImageName.parse("postgres").withTag("13.3-alpine");

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(postgresImage)
                    .withTmpFs(singletonMap("/test_tmpfs", "rw"))
                    .withReuse(true);

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        // Liquibase DataSource
        registry.add("spring.liquibase.url", postgresContainer::getJdbcUrl);
        registry.add("spring.liquibase.user", postgresContainer::getUsername);
        registry.add("spring.liquibase.password", postgresContainer::getPassword);

        // R2DBC DataSource
        Supplier<Object> getR2dbcUrl = () -> format("r2dbc:pool:postgresql://%s:%d/%s",
                postgresContainer.getHost(),
                postgresContainer.getFirstMappedPort(),
                postgresContainer.getDatabaseName());

        registry.add("spring.r2dbc.url", getR2dbcUrl);
        registry.add("spring.r2dbc.username", postgresContainer::getUsername);
        registry.add("spring.r2dbc.password", postgresContainer::getPassword);
    }

    public static void executeSqlScript(Resource script) {
        var connectionFactory =
                ConnectionFactories.get(PostgreSQLR2DBCDatabaseContainer.getOptions(postgresContainer));

        Mono.from(connectionFactory.create())
                .flatMap(c -> ScriptUtils.executeSqlScript(c, script))
                .block();
    }
}
