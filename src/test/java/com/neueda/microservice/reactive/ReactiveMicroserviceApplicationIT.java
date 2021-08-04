package com.neueda.microservice.reactive;

import com.neueda.microservice.reactive.model.Chassis;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Hooks;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ReactiveMicroserviceApplicationIT extends PostgresTestContainer {

	@Autowired
	private WebTestClient webClient;

	@BeforeAll
	static void init(
			@Value("db/delete_all_chassis_entity.sql") Resource deleteScript,
			@Value("db/insert_one_chassis_entity.sql") Resource insertScript) {

		executeSqlScript(deleteScript);
		executeSqlScript(insertScript);
	}

	@BeforeEach
	void setUp() {
		Hooks.onOperatorDebug();
	}

	@Test
	void shouldHaveNoChassis() {
		webClient.get()
				.uri("/api/v1/chassis")
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Chassis.class)
				.hasSize(1);
	}
}
