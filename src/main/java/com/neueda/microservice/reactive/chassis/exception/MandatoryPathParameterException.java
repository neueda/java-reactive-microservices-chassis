package com.neueda.microservice.reactive.chassis.exception;

public class MandatoryPathParameterException extends IllegalArgumentException {

    private final String path;

    public MandatoryPathParameterException(String path, String message) {
        super(message);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
