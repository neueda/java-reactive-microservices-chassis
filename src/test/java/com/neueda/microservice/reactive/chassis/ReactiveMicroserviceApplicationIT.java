package com.neueda.microservice.reactive.chassis;

import com.neueda.microservice.reactive.chassis.model.Chassis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Hooks;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ReactiveMicroserviceApplicationIT extends PostgresTestContainer {

	@BeforeEach
	void setUp(@Autowired DatabaseClient database) {
		Hooks.onOperatorDebug();
		cleanChassisEntityTable(database);
		insertItemInChassisEntityTable(database, new Chassis("integration test", "description text"));
	}

	@Test
	void shouldHaveNoChassis(@Autowired WebTestClient webClient) {
		webClient.get()
				.uri("/v1/chassis")
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Chassis.class)
				.hasSize(1);
	}
}
