package com.neueda.microservice.reactive.client;

import com.neueda.microservice.reactive.properties.ClientProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GitHubClient {

    private final ClientHelper clientHelper;

    public GitHubClient(WebClient.Builder webClientBuilder, ClientProperties props) {
        this.clientHelper = new ClientHelper(
                webClientBuilder
                        .baseUrl(props.baseUrl().toString())
                        .build());
    }

    public Mono<String> searchUsernameContaining(@NonNull String value) {

        return clientHelper.performGetRequest(ub ->
                        ub.pathSegment("search").pathSegment("users")
                                .queryParam("q", value + "+repos:>0")
                                .build(), String.class);
    }
}
