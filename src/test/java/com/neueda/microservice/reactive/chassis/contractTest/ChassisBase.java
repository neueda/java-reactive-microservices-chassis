package com.neueda.microservice.reactive.chassis.contractTest;

import com.neueda.microservice.reactive.chassis.client.GitHubClient;
import com.neueda.microservice.reactive.chassis.controller.ChassisController;
import com.neueda.microservice.reactive.chassis.model.Chassis;
import com.neueda.microservice.reactive.chassis.service.ChassisService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@WebFluxTest(ChassisController.class)
public class ChassisBase {

    @Autowired
    private ChassisController chassisController;

    @MockBean
    private ChassisService chassisService;

    @MockBean
    private GitHubClient gitHubClient;

    @BeforeEach
    public void setUp() {
        RestAssuredWebTestClient.standaloneSetup(chassisController);

        Chassis response = new Chassis("test name", "description test");
        given(chassisService.retrieveAllChassis()).willReturn(Flux.just(response));
    }

    @Test
    public void fakeTest() {
        // create with solo goal to spring boot consider this as a test class
    }
}
