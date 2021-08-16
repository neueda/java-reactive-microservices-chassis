package com.neueda.microservice.reactive.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@RequiredArgsConstructor
abstract class AbstractFunctionalValidator<V extends Validator, B extends BindException> implements FunctionalValidator {

    private final V validator;
    private final Class<B> exception;

    public final <T> Mono<T> valid(T target) {
        try {
            B errors = exception.getDeclaredConstructor(Object.class, String.class)
                    .newInstance(target, target.getClass().getName());
            validator.validate(target, errors);

            return errors.getAllErrors().isEmpty() ? just(target) : error(errors);
        } catch (ReflectiveOperationException e) {
            return error(e);
        }
    }
}
