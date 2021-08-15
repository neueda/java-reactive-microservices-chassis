package com.neueda.microservice.reactive.client;

import com.neueda.microservice.reactive.properties.ClientProperties;
import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.trimWhitespace;

@Service
public class GitHubClient {

    private final ClientHelper clientHelper;


    public GitHubClient(WebClient.Builder webClientBuilder, ClientProperties props,
                        @Value("${logging.level.reactor.netty.http.client:}") String logLevel) {

        if (hasText(logLevel) && trimWhitespace(logLevel).equalsIgnoreCase("debug")) {
            webClientBuilder = webClientBuilder.clientConnector(
                    new ReactorClientHttpConnector(HttpClient.create().wiretap(
                            "reactor.netty.http.client.HttpClient",
                            LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)));
        }

        this.clientHelper =
                new ClientHelper(webClientBuilder
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
