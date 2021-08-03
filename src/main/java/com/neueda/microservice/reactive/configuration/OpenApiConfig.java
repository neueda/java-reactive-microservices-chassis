package com.neueda.microservice.reactive.configuration;

import com.neueda.microservice.reactive.properties.ApiInfoProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final ApiInfoProperties props;

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(
                new Info().title(props.title())
                        .description(props.description())
                        .version(props.version()));
    }
}
