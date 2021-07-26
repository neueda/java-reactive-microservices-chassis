package com.neueda.microservice.reactive.chassis.model;


import javax.validation.constraints.NotBlank;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.StringUtils.hasText;

public record Chassis(
        @NotBlank(message = "Name is mandatory")
        String name,
        String description) {

    public Chassis {
        requireNonNull(name);
        if (name.isBlank())
            throw new IllegalArgumentException("Name cannot be blank");
    }

    public Chassis(String name) {
        this(name, null);
    }
}
