package com.neueda.microservice.reactive.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@TestConfiguration(proxyBeanMethods = false)
public class RestDocsConfig {

    @Bean
    WebTestClientBuilderCustomizer webTestClientBuilderCustomizer() {
        return builder -> builder.entityExchangeResultConsumer(document("{class-name}/{method-name}"));
    }
}
