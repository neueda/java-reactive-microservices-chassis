package com.neueda.microservice.reactive.exception;

public class MandatoryPathParameterException extends InvalidParameterException {

    public MandatoryPathParameterException(String path, String message) {
        super(path, message);
    }
}
