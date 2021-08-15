package com.neueda.microservice.reactive.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class DefaultFunctionalValidator extends AbstractFunctionalValidator<Validator, ValidationFailedException> {

    public DefaultFunctionalValidator(Validator validator) {
        super(validator, ValidationFailedException.class);
    }
}
