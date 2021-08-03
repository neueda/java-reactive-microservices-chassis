package com.neueda.microservice.reactive.contractsBase;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.springframework.web.context.WebApplicationContext;

abstract class ContractTestSetup {
    void standaloneSetup(Object... controllers) {
        RestAssuredWebTestClient.standaloneSetup(controllers);
    }

    void webAppContextSetup(WebApplicationContext webApplicationContext) {
        RestAssuredWebTestClient.webAppContextSetup(webApplicationContext);
    }
}
