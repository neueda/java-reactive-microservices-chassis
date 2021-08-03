package com.neueda.microservice.reactive.chassis.controller;

import com.neueda.microservice.reactive.chassis.client.GitHubClient;
import com.neueda.microservice.reactive.chassis.model.Chassis;
import com.neueda.microservice.reactive.chassis.service.ChassisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest
@AutoConfigureRestDocs
class ChassisControllerTests {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ChassisService chassisService;

    @MockBean
    private GitHubClient gitHubClient;

    @Test
    void shouldRetrieveAllChassis() {
        // given
        var chassis = new Chassis("Chassis Under Test", "Description Text");
        given(chassisService.retrieveAllChassis())
                .willReturn(Flux.just(chassis));

        // when
        webClient.get()
                .uri("/v1/chassis")
                .accept(APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBodyList(Chassis.class)
                .consumeWith(document("list-chassis"))
                .contains(chassis);
    }

    @Test
    void shouldRetrieveAllChassisClientItems() {
        // given
        var clientResponse = "Client Response";
        given(gitHubClient.searchUsernameContaining(anyString()))
                .willReturn(Mono.just(clientResponse));

        // when
        webClient.get()
                .uri("/v1/chassisClient/clientUsername")
                .accept(APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(document("github-user-search"))
                .isEqualTo(clientResponse);
    }
}
