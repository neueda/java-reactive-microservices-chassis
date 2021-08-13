package com.neueda.microservice.reactive.exception;

import static java.lang.String.format;

public class MissingQueryParameterException extends IllegalArgumentException {

    public MissingQueryParameterException(String parameterName, String parameterType) {
        super(format("Required query parameter '%s' for method parameter type %s is not present",
                parameterName, parameterType));
    }
}
