package com.neueda.microservice.reactive.validation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ValidationFailedException extends BindException {
    /**
     * Create a new BindException instance for a target bean.
     *
     * @param target     the target bean to bind onto
     * @param objectName the name of the target object
     * @see org.springframework.validation.BeanPropertyBindingResult
     */
    ValidationFailedException(Object target, String objectName) {
        super(target, objectName);
    }
}
