package com.neueda.microservice.reactive.chassis.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
class ClientHelper {

    private final WebClient webClient;

    <T> Mono<T> performGetRequest(URI uri, Class<T> returnType) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(returnType);
    }
}
