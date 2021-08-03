package com.neueda.microservice.reactive.chassis.client;

import com.neueda.microservice.reactive.chassis.exception.MandatoryPathParameterException;
import com.neueda.microservice.reactive.chassis.properties.ClientProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
public class GitHubClient {

    private final ClientHelper clientHelper;

    public GitHubClient(WebClient.Builder webClientBuilder, ClientProperties props) {
        this.clientHelper = new ClientHelper(
                webClientBuilder
                        .baseUrl(props.baseUrl().toString())
                        .build());
    }

    public Mono<String> searchUsernameContaining(String value) {
        if (!StringUtils.hasText(value)) {
            throw new MandatoryPathParameterException("chassisClientNameContain/{usernamePart}",
                    format("'username' have to have length greater than 0, " +
                            "and contains at least one non-whitespace character. Current value: [%s]", value));
        }

        return clientHelper.performGetRequest(
                uriBuilder -> uriBuilder.pathSegment("search").pathSegment("users")
                        .queryParam("q", value + "+repos:>0")
                        .build(),
                String.class);
    }
}
