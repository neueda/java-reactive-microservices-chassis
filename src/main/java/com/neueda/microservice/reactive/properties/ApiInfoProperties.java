package com.neueda.microservice.reactive.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("apidoc")
@ConstructorBinding
@Validated
public record ApiInfoProperties(
        @NotBlank(message = "apidoc.title is mandatory") String title,
        @NotBlank(message = "apidoc.description is mandatory") String description,
        @NotBlank(message = "apidoc.version is mandatory") String version) {
}

