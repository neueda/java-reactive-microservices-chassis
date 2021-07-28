package com.neueda.microservice.reactive.chassis.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;

@RequiredArgsConstructor
class ClientHelper {

    private final WebClient webClient;

    <T> Mono<T> performGetRequest(URI uri, Class<T> clazz) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(clazz)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(3)));
    }
}
