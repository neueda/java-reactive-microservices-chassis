package com.neueda.microservice.reactive.contracts;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.configuration.ChassisRouterConfig;
import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.handler.ChassisRouterHandler;
import com.neueda.microservice.reactive.service.ChassisService;
import com.neueda.microservice.reactive.validation.DefaultFunctionalValidator;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@Import({ChassisRouterConfig.class,
        ChassisRouterHandler.class,
        DefaultFunctionalValidator.class})
abstract class ChassisBase extends ContractTest {

    @MockBean
    private ChassisService chassisService;

    @MockBean
    private GitHubClient gitHubClient;

    @BeforeEach
    void setUp() {
        // init
        webTestClientSetup();

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
