package com.neueda.microservice.reactive.controller;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.configuration.ChassisRouter;
import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.handler.ChassisHandler;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.service.ChassisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTypeExcludeFilter;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@Import({ChassisRouter.class, ChassisHandler.class})
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
        var chassisEntity =
                new ChassisEntity()
                        .setId(1L)
                        .setName("Chassis Under Test")
                        .setDescription("Description Text");

        given(chassisService.retrieveAllChassis())
                .willReturn(Flux.just(chassisEntity));

        // when
        webClient.get()
                .uri("/api/v1/chassis")
                .accept(APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBodyList(Chassis.class)
                .consumeWith(document("list-chassis"))
                .contains(new Chassis("Chassis Under Test", "Description Text"));
    }

    @Test
    void shouldRetrieveAllChassisClientItems() {
        // given
        var expected = "{\"total_count\":0,\"incomplete_results\":false,\"items\":[]}";
        given(gitHubClient.searchUsernameContaining(anyString()))
                .willReturn(Mono.just(expected));

        // when
        webClient.get()
                .uri("/api/v1/chassisClientNameContain/clientPartialNameTest")
                .accept(APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(document("github-user-search"))
                .isEqualTo(expected);
    }
}
