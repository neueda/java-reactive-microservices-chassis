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

    private static String getR2dbcUrl() {
        return format("r2dbc:pool:postgresql://%s:%d/%s",
                postgresContainer.getHost(),
                postgresContainer.getFirstMappedPort(),
                postgresContainer.getDatabaseName());
    }

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {

        // Liquibase DataSource
        registry.add("spring.liquibase.url", postgresContainer::getJdbcUrl);
        registry.add("spring.liquibase.user", postgresContainer::getDatabaseName);
        registry.add("spring.liquibase.password", postgresContainer::getDatabaseName);

        // R2DBC DataSource
        registry.add("spring.r2dbc.url", PostgresTestContainer::getR2dbcUrl);
        registry.add("spring.r2dbc.username", postgresContainer::getDatabaseName);
        registry.add("spring.r2dbc.password", postgresContainer::getDatabaseName);
    }

    public static void executeSqlScript(Resource script) {
        var connectionFactory =
                ConnectionFactories.get(PostgreSQLR2DBCDatabaseContainer.getOptions(postgresContainer));

        Mono.from(connectionFactory.create())
                .flatMap(c -> ScriptUtils.executeSqlScript(c, script))
                .block();
    }
}
