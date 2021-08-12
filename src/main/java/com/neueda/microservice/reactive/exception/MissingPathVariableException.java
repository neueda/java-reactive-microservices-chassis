package com.neueda.microservice.reactive.exception;

public class MissingPathVariableException extends IllegalArgumentException {

    public MissingPathVariableException(String variableName, String variableType) {
        super("Required path variable '" + variableName + "' for method parameter type " + variableType + " is not present");
    }
}
