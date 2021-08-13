package com.neueda.microservice.reactive.exception;

import java.util.NoSuchElementException;

public class ItemNotFoundException extends NoSuchElementException {

    public ItemNotFoundException(String message) {
        super(message);
    }
}
