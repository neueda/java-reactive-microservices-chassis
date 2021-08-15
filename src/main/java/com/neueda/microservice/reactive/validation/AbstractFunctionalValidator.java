package com.neueda.microservice.reactive.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
abstract class AbstractFunctionalValidator<U extends Validator, E extends BindException> implements FunctionalValidator {

    private final U validator;
    private final Class<E> exception;

    public final <T> Mono<T> valid(T target) {
        try {
            E errors = exception.getDeclaredConstructor(Object.class, String.class)
                    .newInstance(target, target.getClass().getName());

            validator.validate(target, errors);
            return errors.getAllErrors().isEmpty()
                    ? Mono.just(target)
                    : Mono.error(errors);
        } catch (ReflectiveOperationException e) {
            return Mono.error(e);
        }
    }
}
