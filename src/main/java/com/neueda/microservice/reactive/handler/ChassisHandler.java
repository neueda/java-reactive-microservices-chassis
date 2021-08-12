package com.neueda.microservice.reactive.handler;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.exception.EntityNotFoundException;
import com.neueda.microservice.reactive.exception.IdFormatException;
import com.neueda.microservice.reactive.exception.MissingPathVariableException;
import com.neueda.microservice.reactive.exception.MissingQueryParameterException;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.model.ErrorResponse;
import com.neueda.microservice.reactive.service.ChassisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.Long.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class ChassisHandler {

    private final ChassisService chassisService;
    private final GitHubClient gitHubClient;

    private final Function<ChassisEntity, Chassis> toChassisModel =
            e -> new Chassis(e.getName(), e.getDescription());

    public Mono<ServerResponse> getChassisItem(ServerRequest request) {

        return Mono.just(request.pathVariable("id"))
                .flatMap(stringId -> isLong(stringId)
                        ? Mono.just(valueOf(stringId))
                        : Mono.error(new IdFormatException("/api/v1/chassis/" + stringId, new NumberFormatException())))
                .flatMap(chassisService::getChassisById)
                .map(toChassisModel)
                .flatMap(chassis -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(chassis));
    }

    public Mono<ServerResponse> listChassisItem(ServerRequest request) {

        return ok()
                .contentType(APPLICATION_JSON)
                .body(chassisService.retrieveAllChassis().map(toChassisModel), Chassis.class);
    }

    public Mono<ServerResponse> createChassisItem(ServerRequest request) {

        return request.bodyToMono(Chassis.class)
                .flatMap(chassisService::addChassis)
                .flatMap(entity -> created(URI.create("/chassis/" + entity.getId()))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new Chassis(entity.getName(), entity.getDescription())));
    }

    public Mono<ServerResponse> listChassisContainingName(ServerRequest request) {

        return Mono.just(request.queryParam("value"))
                .filter(Optional::isPresent)
                .switchIfEmpty(
                        Mono.error(new MissingQueryParameterException("value", String.class.getTypeName())))
                .map(Optional::get)
                .flatMap(v -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(chassisService.searchChassisByNameContaining(v).map(toChassisModel), Chassis.class));
    }

    public Mono<ServerResponse> getChassisWebClientResponse(ServerRequest request) {

        return Mono.just(request.pathVariable("usernamePart"))
                .filter(StringUtils::hasText)
                .flatMap(gitHubClient::searchUsernameContaining)
                .flatMap(v -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(v));
    }

    public Mono<ServerResponse> validUsernamePart(ServerRequest request) {
        return Mono.error(new MissingPathVariableException("usernamePart", String.class.getTypeName()));
    }


    public Mono<ServerResponse> errorFilter(ServerRequest request, HandlerFunction<ServerResponse> next) {

        //ToDo: This filter exception handler must me improved
        return next.handle(request).log()
                .onErrorResume(IdFormatException.class, ex -> badRequest()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(
                                ex.getLocalizedMessage(),
                                request.path(),
                                ex.getClass().getTypeName())))
                .onErrorResume(MissingPathVariableException.class, ex -> badRequest()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(
                                ex.getLocalizedMessage(),
                                request.path(),
                                ex.getClass().getTypeName())))
                .onErrorResume(MissingQueryParameterException.class, ex -> badRequest()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(
                                ex.getLocalizedMessage(),
                                request.path(),
                                ex.getClass().getTypeName())))
                .onErrorResume(EntityNotFoundException.class, ex -> notFound().build());
    }

    private boolean isLong(String strNum) {
        try {
            Long.parseLong(strNum);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
