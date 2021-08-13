package com.neueda.microservice.reactive.controller;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.configuration.ChassisRouteConfig;
import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.handler.ChassisRouteHandler;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.service.ChassisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentationConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@Import({ChassisRouteConfig.class, ChassisRouteHandler.class})
@WebFluxTest
@AutoConfigureRestDocs
class ChassisControllerTests {

    @MockBean
    private ChassisService chassisService;

    @MockBean
    private GitHubClient gitHubClient;

    private WebTestClient webClient;

    @BeforeEach
    void setUp(
            @Autowired ApplicationContext context,
            @Autowired WebTestClientRestDocumentationConfigurer configurer) {

        webClient = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
                .filter(configurer)
                .entityExchangeResultConsumer(document("{class-name}/{method-name}"))
                .build();
    }

    @Test
    void shouldRetrieveAllChassis() {
        // given
        var chassisEntity =
                new ChassisEntity()
                        .setId(1L)
                        .setName("Chassis Under Test")
                        .setDescription("Description Text");

        given(chassisService.findAllChassisItem())
                .willReturn(Flux.just(chassisEntity));

        // when
        webClient.get()
                .uri("/api/v1/chassis")
                .accept(APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBodyList(Chassis.class)
                .contains(new Chassis("Chassis Under Test", "Description Text"));
    }

    @Test
    void shouldRetrieveChassisClientResponse() {
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
                .isEqualTo(expected);
    }
}
