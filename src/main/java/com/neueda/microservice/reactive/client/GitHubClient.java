package com.neueda.microservice.reactive.client;

import com.neueda.microservice.reactive.properties.ClientProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GitHubClient {

    private final ClientHelper clientHelper;


    public GitHubClient(WebClient.Builder webClientBuilder, ClientProperties clientProps,
                        @Value("${logging.level.reactor.netty.http.client:}") String logLevel) {

        this.clientHelper = new ClientHelper(webClientBuilder, clientProps, logLevel);
    }

    public Mono<String> searchUsernameContaining(@NonNull String value) {
        return clientHelper.performGetRequest(ub ->
                        ub.pathSegment("search").pathSegment("users")
                                .queryParam("q", value.concat("+repos:>0"))
                                .build(), String.class);
    }
}
