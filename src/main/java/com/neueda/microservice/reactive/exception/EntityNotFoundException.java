package com.neueda.microservice.reactive.exception;

public class EntityNotFoundException extends InvalidParameterException {

    public EntityNotFoundException(String path, String message) {
        super(path, message);
    }
}
