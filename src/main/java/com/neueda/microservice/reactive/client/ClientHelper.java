package com.neueda.microservice.reactive.client;

import com.neueda.microservice.reactive.properties.ClientProperties;
import io.netty.handler.logging.LogLevel;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.net.URI;
import java.time.Duration;
import java.util.function.Function;

import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.trimWhitespace;
import static reactor.util.retry.Retry.backoff;

class ClientHelper {

    private final WebClient webClient;

    ClientHelper(WebClient.Builder webClientBuilder, ClientProperties props, String logLevel) {
        if (isLogEnabled(logLevel)) {
            webClientBuilder.clientConnector(
                    new ReactorClientHttpConnector(HttpClient.create().wiretap(
                            "reactor.netty.http.client.HttpClient",
                            LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)));
        }

        this.webClient = webClientBuilder
                .baseUrl(props.baseUrl().toString())
                .build();
    }

    private boolean isLogEnabled(String logLevel) {
        logLevel = trimWhitespace(logLevel);
        return hasText(logLevel) &&
                (logLevel.equalsIgnoreCase("debug") || logLevel.equalsIgnoreCase("trace"));
    }

    <T> Mono<T> performGetRequest(Function<UriBuilder, URI> uriFunction, Class<T> clazz) {
        return webClient.get()
                .uri(uriFunction)
                .retrieve()
                .bodyToMono(clazz)
                .retryWhen(backoff(3, Duration.ofSeconds(3)));
    }
}
