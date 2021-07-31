package com.neueda.microservice.reactive.chassis.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.util.function.Function;

@RequiredArgsConstructor
class ClientHelper {

    private final WebClient webClient;

    <T> Mono<T> performGetRequest(Function<UriBuilder, URI> uriFunction, Class<T> clazz) {
        return webClient.get()
                .uri(uriFunction)
                .retrieve()
                .bodyToMono(clazz)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(3)));
    }
}
