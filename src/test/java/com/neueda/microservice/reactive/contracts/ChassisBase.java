package com.neueda.microservice.reactive.contracts;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.configuration.ChassisRouterConfig;
import com.neueda.microservice.reactive.configuration.RestDocsConfig;
import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.handler.ChassisRouteHandler;
import com.neueda.microservice.reactive.service.ChassisService;
import com.neueda.microservice.reactive.validation.DefaultFunctionalValidator;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@Tag("ContractTest")
@WebFluxTest
@AutoConfigureRestDocs
@Import({RestDocsConfig.class, DefaultFunctionalValidator.class,
        ChassisRouteHandler.class, ChassisRouterConfig.class})
abstract class ChassisBase {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ChassisService chassisService;

    @MockBean
    private GitHubClient gitHubClient;

    @BeforeEach
    void setUp() {
        // setup
        RestAssuredWebTestClient.webTestClient(webClient);

        // given
        var chassisEntity =
                new ChassisEntity()
                        .setId(1L)
                        .setName("test name")
                        .setDescription("description test");

        given(chassisService.findAllChassisItems())
                .willReturn(Flux.just(chassisEntity));
    }
}
