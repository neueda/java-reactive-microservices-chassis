package com.neueda.microservice.reactive.handler;

import com.neueda.microservice.reactive.exception.ParameterFormatException;
import com.neueda.microservice.reactive.model.ErrorResponse;
import reactor.core.publisher.Mono;


public class HandlerHelper {

    public static final String VAR_USERNAME_CONTAINS = "usernameContaining";

    static Mono<Long> parseLong(String strNum) {
        try {
            return Mono.just(Long.parseLong(strNum));
        } catch (NumberFormatException nfe) {
            return Mono.error(new ParameterFormatException(strNum, Long.class.getTypeName(), nfe));
        }
    }

    static Mono<ErrorResponse> createErrorRespondAndLog(Exception ex, String path) {
        return Mono
                .just(new ErrorResponse(ex.getLocalizedMessage(), path, ex.getClass().getTypeName()))
                .log();
    }
}
