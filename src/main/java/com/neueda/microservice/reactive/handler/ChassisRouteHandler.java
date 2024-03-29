package com.neueda.microservice.reactive.handler;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.exception.ItemNotFoundException;
import com.neueda.microservice.reactive.exception.MissingPathVariableException;
import com.neueda.microservice.reactive.exception.MissingQueryParameterException;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.model.ErrorResponse;
import com.neueda.microservice.reactive.service.ChassisService;
import com.neueda.microservice.reactive.validation.FunctionalValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.neueda.microservice.reactive.handler.HandlerHelper.VAR_IN_USERNAME;
import static com.neueda.microservice.reactive.handler.HandlerHelper.buildErrorResponse;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Stream.generate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Component
@RequiredArgsConstructor
public class ChassisRouteHandler {

    private final ChassisService chassisService;
    private final GitHubClient gitHubClient;
    private final FunctionalValidator validator;

    private final Function<ChassisEntity, Chassis> toChassisModel =
            e -> new Chassis(e.getName(), e.getDescription());

    private final BiFunction<ServerRequest, ChassisEntity, URI> toUri =
            (r,i) -> fromPath(r.path())
                    .pathSegment(i.getId().toString())
                    .build().toUri();

    public Mono<ServerResponse> getChassisItem(ServerRequest request) {
        // tag::retrieve[]
        return just(request.pathVariable("id"))
                .flatMap(HandlerHelper::parseLong)
                .flatMap(chassisService::getChassisItemById)
                .map(toChassisModel) // <1>
                .flatMap(chassis -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(chassis));
        // end::retrieve[]
    }

    public Mono<ServerResponse> listChassisItems(ServerRequest request) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(chassisService.findAllChassisItems().map(toChassisModel), Chassis.class);
    }

    public Mono<ServerResponse> createChassisItem(ServerRequest request) {
        // tag::create[]
        return request.bodyToMono(Chassis.class)
                .flatMap(validator::valid) // <1>
                .flatMap(chassisService::addChassisItem) // <2>
                .flatMap(entity -> created(toUri.apply(request, entity))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new Chassis(entity.getName(), entity.getDescription())));
        // end::create[]
    }

    public Mono<ServerResponse> listChassisItemsContainingName(ServerRequest request) {
        String paramName = "value";
        return just(request.queryParam(paramName))
                .filter(Optional::isPresent)
                .switchIfEmpty(error(new MissingQueryParameterException(paramName, String.class.getTypeName())))
                .map(Optional::get)
                .flatMap(v -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(chassisService.findAllChassisItemsByNameContaining(v).map(toChassisModel), Chassis.class));
    }

    public Mono<ServerResponse> getChassisClientResponse(ServerRequest request) {
        return just(request.pathVariable(VAR_IN_USERNAME))
                .filter(StringUtils::hasText)
                .flatMap(gitHubClient::searchUsernameContaining)
                .flatMap(v -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(v));
    }

    public Mono<ServerResponse> invalidClientNamePath(ServerRequest request) {
        return error(new MissingPathVariableException(request.path(), VAR_IN_USERNAME));
    }

    public Mono<ServerResponse> infiniteStream(ServerRequest r) {
        return ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(Flux
                        .fromStream(generate(LocalDateTime::now))
                        .delayElements(ofSeconds(1))
                        .subscribeOn(Schedulers.boundedElastic()),
                        LocalDateTime.class);
    }

    public Mono<ServerResponse> errorHandlerFilter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        return next.handle(request)
                .onErrorResume(IllegalArgumentException.class,
                        ex -> badRequest()
                                .contentType(APPLICATION_JSON)
                                .body(buildErrorResponse(ex, request.path()).log(), ErrorResponse.class))
                .onErrorResume(ItemNotFoundException.class,
                        ex -> notFound().build().log());
    }
}
