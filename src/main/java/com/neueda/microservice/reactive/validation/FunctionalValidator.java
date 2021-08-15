package com.neueda.microservice.reactive.validation;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface FunctionalValidator {
    <T> Mono<T> valid(T target);
}
