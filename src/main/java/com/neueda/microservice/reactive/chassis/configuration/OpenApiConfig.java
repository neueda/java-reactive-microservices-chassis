package com.neueda.microservice.reactive.chassis.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(
                new Info().title("Java Microservice Chassis")
                        .description("Microservice Chassis for Reactive Restful API with Spring Boot")
                        .version("0.0.1"));
    }
}
