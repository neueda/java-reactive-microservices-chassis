package com.neueda.microservice.reactive.handler;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.configuration.ChassisRouterConfig;
import com.neueda.microservice.reactive.configuration.RestDocsConfig;
import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.service.ChassisService;
import com.neueda.microservice.reactive.validation.DefaultFunctionalValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest
@AutoConfigureRestDocs
@Import({RestDocsConfig.class, DefaultFunctionalValidator.class,
        ChassisRouteHandler.class, ChassisRouterConfig.class})
class ChassisRouteHandlerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ChassisService chassisService;

    @MockBean
    private GitHubClient gitHubClient;

    @Test
    @DisplayName("Should retrieve all chassis items")
    void shouldRetrieveAllChassisItems() {
        // given
        var chassisEntity =
                new ChassisEntity()
                        .setId(1L)
                        .setName("Chassis Under Test")
                        .setDescription("Description Text");

        given(chassisService.findAllChassisItems())
                .willReturn(Flux.just(chassisEntity));

        // when
        webTestClient.get()
                .uri("/api/v1/chassis")
                .accept(APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBodyList(Chassis.class)
                .contains(new Chassis("Chassis Under Test", "Description Text"));
    }

    @Test
    @DisplayName("Should retrieve chassis client response")
    void shouldRetrieveChassisClientResponse() {
        // given
        var expected = "{\"total_count\":0,\"incomplete_results\":false,\"items\":[]}";
        given(gitHubClient.searchUsernameContaining(anyString()))
                .willReturn(Mono.just(expected));

        // when
        webTestClient.get()
                .uri("/api/v1/chassis/client/nameContain/clientPartialNameTest")
                .accept(APPLICATION_JSON)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expected);
    }
}
