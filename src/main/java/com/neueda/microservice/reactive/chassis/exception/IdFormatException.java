package com.neueda.microservice.reactive.chassis.exception;

public class IdFormatException extends NumberFormatException {

    private final String path;

    public IdFormatException(NumberFormatException e, String path) {
        super(e.getLocalizedMessage() + ". Chassis id in a wrong format");
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
