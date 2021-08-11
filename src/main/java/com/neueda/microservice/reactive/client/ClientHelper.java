package com.neueda.microservice.reactive.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.function.Function;

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
}
