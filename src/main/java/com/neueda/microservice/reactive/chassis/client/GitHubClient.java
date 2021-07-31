package com.neueda.microservice.reactive.chassis.client;

import com.neueda.microservice.reactive.chassis.exception.MandatoryPathParameterException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static java.lang.String.format;
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
        if (!StringUtils.hasText(username)) {
            throw new MandatoryPathParameterException("chassisClient/{username}",
                    format("'username' have to have length greater than 0, " +
                            "and contains at least one non-whitespace character. Current value: [%s]", username));
        }

        return clientHelper.performGetRequest(fromUri(baseUri)
                .path("/users")
                .queryParam("q", username + "+repos:>0")
                .build()
                .toUri(), String.class);
    }
}
