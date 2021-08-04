package com.neueda.microservice.reactive.contractsBase;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.Tag;
import org.springframework.web.context.WebApplicationContext;

@Tag("ContractTest")
abstract class ContractTest {
    void standaloneSetup(Object... controllers) {
        RestAssuredWebTestClient.standaloneSetup(controllers);
    }

    void webAppContextSetup(WebApplicationContext webApplicationContext) {
        RestAssuredWebTestClient.webAppContextSetup(webApplicationContext);
    }
}
