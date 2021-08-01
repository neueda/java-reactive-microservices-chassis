package com.neueda.microservice.reactive.chassis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.net.URI;

@ConfigurationProperties("client")
@ConstructorBinding
@Validated
public record ClientProperties(
        @NotNull(message = "client.url is mandatory") URI baseUrl) {
}
