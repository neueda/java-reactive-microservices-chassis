package com.neueda.microservice.reactive.chassis.contractsBase;

import com.neueda.microservice.reactive.chassis.client.GitHubClient;
import com.neueda.microservice.reactive.chassis.controller.ChassisController;
import com.neueda.microservice.reactive.chassis.model.Chassis;
import com.neueda.microservice.reactive.chassis.service.ChassisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@WebFluxTest(ChassisController.class)
@Tag("ContractBase")
public abstract class ChassisBase extends ContractTestSetup {

    @Autowired
    private ChassisController chassisController;

    @MockBean
    private ChassisService chassisService;

    @MockBean
    private GitHubClient gitHubClient;

    @BeforeEach
    public void setUp() {
        Chassis response = new Chassis("test name", "description test");
        given(chassisService.retrieveAllChassis()).willReturn(Flux.just(response));

        standaloneSetup(chassisController);
    }
}