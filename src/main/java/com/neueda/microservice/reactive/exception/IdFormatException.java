package com.neueda.microservice.reactive.exception;

public class IdFormatException extends NumberFormatException {

    private final String path;

    public IdFormatException(String path, NumberFormatException cause) {
        super(cause.getLocalizedMessage() + ". Chassis id in a wrong format");
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
