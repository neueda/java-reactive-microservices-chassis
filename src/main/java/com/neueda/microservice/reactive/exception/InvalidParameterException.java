package com.neueda.microservice.reactive.exception;

public class InvalidParameterException extends IllegalArgumentException {

    private final String path;

    public InvalidParameterException(String path, String message) {
        super(message);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
