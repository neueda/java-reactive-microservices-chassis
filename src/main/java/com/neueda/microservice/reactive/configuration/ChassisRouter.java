package com.neueda.microservice.reactive.configuration;

import com.neueda.microservice.reactive.handler.ChassisHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ChassisRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(ChassisHandler handler) {
        return route()
                .path("/api/v1", b1 -> b1
                        .path("/chassis", b2 -> b2
                                .GET("/{id}", handler::getChassisItem)
                                .GET(handler::listChassisItem)
                                .POST(handler::createChassisItem))
                        .GET("/chassisNameContain", handler::listChassisContainingName)
                        .GET("/chassisClientNameContain", handler::validUsernamePart)
                        .GET("/chassisClientNameContain/{usernamePart}", handler::getChassisWebClientResponse))
                .filter(handler::errorFilter)
                .build();
    }
}
