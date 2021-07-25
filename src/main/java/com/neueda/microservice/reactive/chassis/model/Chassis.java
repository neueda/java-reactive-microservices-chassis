package com.neueda.microservice.reactive.chassis.model;


import javax.validation.constraints.NotBlank;

import static org.springframework.util.StringUtils.hasText;

public record Chassis(
        @NotBlank(message = "Name is mandatory")
        String name,
        String description) {

    public Chassis { hasText(name); }

    public Chassis(String name) { this(name, null); }
}
