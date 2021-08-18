package com.neueda.microservice.reactive.handler;

import com.neueda.microservice.reactive.exception.ParameterFormatException;
import com.neueda.microservice.reactive.model.ErrorResponse;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;


public class HandlerHelper {

    public static final String VAR_IN_USERNAME = "inUsername";

    static Mono<Long> parseLong(String strNum) {
        try {
            return just(Long.parseLong(strNum));
        } catch (NumberFormatException ex) {
            return error(new ParameterFormatException(strNum, Long.class.getTypeName(), ex));
        }
    }

    static Mono<ErrorResponse> buildErrorResponse(Exception ex, String path) {
        return just(new ErrorResponse(ex.getLocalizedMessage(), path, ex.getClass().getTypeName()));
    }
}
