package com.neueda.microservice.reactive.exception;

public class MissingQueryParameterException extends IllegalArgumentException {

    public MissingQueryParameterException(String parameterName, String parameterType) {
        super("Required query parameter '" + parameterName + "' for method parameter type " + parameterType + " is not present");
    }
}
