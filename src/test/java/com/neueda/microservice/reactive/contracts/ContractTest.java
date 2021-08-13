package com.neueda.microservice.reactive.contracts;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentationConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@Tag("ContractTest")
@WebFluxTest
@AutoConfigureRestDocs
abstract class ContractTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private WebTestClientRestDocumentationConfigurer configurer;

    void webTestClientSetup() {
        RestAssuredWebTestClient.webTestClient(
                WebTestClient
                        .bindToApplicationContext(context)
                        .configureClient()
                        .filter(configurer)
                        .entityExchangeResultConsumer(document("{class-name}/{method-name}"))
                        .build());
    }
}
