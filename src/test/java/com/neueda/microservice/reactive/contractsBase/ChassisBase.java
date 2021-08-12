package com.neueda.microservice.reactive.contractsBase;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.configuration.ChassisRouter;
import com.neueda.microservice.reactive.controller.ChassisController;
import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.handler.ChassisHandler;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.service.ChassisService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@Import({ChassisRouter.class, ChassisHandler.class})
@WebFluxTest
abstract class ChassisBase extends ContractTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ChassisService chassisService;

    @MockBean
    private GitHubClient gitHubClient;

    @BeforeEach
    public void setUp() {
        var chassisEntity =
                new ChassisEntity()
                        .setId(1L)
                        .setName("test name")
                        .setDescription("description test");

        given(chassisService.retrieveAllChassis())
                .willReturn(Flux.just(chassisEntity));

        webTestClientSetup(webClient);
    }
}
