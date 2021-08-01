package com.neueda.microservice.reactive.chassis.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.neueda.microservice.reactive.chassis.properties.ClientProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringJUnitConfig
@AutoConfigureWireMock
public class GitHubClientTests {

    private static GitHubClient client;

    @BeforeAll
    static void init(@Autowired WireMockServer server) {
        URI baseUri = URI.create(server.baseUrl());
        client = new GitHubClient(WebClient.builder(), new ClientProperties(baseUri));
    }

    @Test
    void shouldReturnUsersFound() {
        // given
        var testQuery = "testuser";
        var testUrl = format("/search/users?q=%s+repos:%%3E0", testQuery);
        var expected = "{\"total_count\":0,\"incomplete_results\":false,\"items\":[]}";
        stubFor(get(urlEqualTo(testUrl))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(expected)));

        // when
        String response = client.searchUser(testQuery).block();

        // then
        then(response).isEqualTo(expected);
        verify(getRequestedFor(urlEqualTo(testUrl)));
    }
}
