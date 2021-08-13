package com.neueda.microservice.reactive.exception;

import static java.lang.String.format;

public class MissingPathVariableException extends IllegalArgumentException {

    public MissingPathVariableException(String baseUrl, String variableName) {
        super(format("For path %s/{%s}", baseUrl, variableName));
    }
}
