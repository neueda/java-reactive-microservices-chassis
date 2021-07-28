package com.neueda.microservice.reactive.chassis.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Service
public class GitHubClient {

    private final URI baseUri;
    private final ClientHelper clientHelper;

    public GitHubClient(WebClient.Builder webClientBuilder) {
        this.baseUri = URI.create("https://api.github.com/search");
        this.clientHelper = new ClientHelper(
                webClientBuilder
                        .baseUrl(baseUri.toString())
                        .build());
    }

    public Mono<String> searchUser(String username) {
        URI uri = fromUri(baseUri).path("/users")
                .queryParam("q", username + "+repos:>0")
                .build()
                .toUri();

        return clientHelper.performGetRequest(uri, String.class);
    }
}
