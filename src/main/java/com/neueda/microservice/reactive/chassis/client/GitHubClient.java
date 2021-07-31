package com.neueda.microservice.reactive.chassis.client;

import com.neueda.microservice.reactive.chassis.exception.MandatoryPathParameterException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
public class GitHubClient {

    private final ClientHelper clientHelper;

    public GitHubClient(WebClient.Builder webClientBuilder) {
        this.clientHelper = new ClientHelper(
                webClientBuilder
                        .baseUrl("https://api.github.com/search")
                        .build());
    }

    public Mono<String> searchUser(String username) {
        if (!StringUtils.hasText(username)) {
            throw new MandatoryPathParameterException("chassisClient/{username}",
                    format("'username' have to have length greater than 0, " +
                            "and contains at least one non-whitespace character. Current value: [%s]", username));
        }

        return clientHelper.performGetRequest(
                uriBuilder -> uriBuilder.pathSegment("/users")
                        .queryParam("q", username + "+repos:>0")
                        .build(),
                String.class);
    }
}
