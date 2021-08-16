package com.neueda.microservice.reactive.model;

import javax.validation.constraints.NotBlank;

import static java.util.Objects.requireNonNull;

public record Chassis(
        @NotBlank(message = "Name is mandatory")
        String name,
        String description) {

    public Chassis(String name) {
        this(name, null);
    }

    public Chassis {
        requireNonNull(name);
    }
}
