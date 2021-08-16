package com.neueda.microservice.reactive.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.function.Function;

import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.trimWhitespace;
import static reactor.util.retry.Retry.backoff;

@RequiredArgsConstructor
class ClientHelper {

    private final WebClient webClient;

    <T> Mono<T> performGetRequest(Function<UriBuilder, URI> uriFunction, Class<T> clazz) {
        return webClient.get()
                .uri(uriFunction)
                .retrieve()
                .bodyToMono(clazz)
                .retryWhen(backoff(3, Duration.ofSeconds(3)));
    }

    static boolean isLogEnabled(String logLevel) {
        logLevel = trimWhitespace(logLevel);
        return hasText(logLevel) &&
                (logLevel.equalsIgnoreCase("debug") || logLevel.equalsIgnoreCase("trace"));
    }
}
