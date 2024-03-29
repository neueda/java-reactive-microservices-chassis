package com.neueda.microservice.reactive.configuration;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.handler.ChassisRouteHandler;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.model.ErrorResponse;
import com.neueda.microservice.reactive.service.ChassisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.neueda.microservice.reactive.handler.HandlerHelper.VAR_IN_USERNAME;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration(proxyBeanMethods = false)
public class ChassisRouterConfig {
    @RouterOperations({
            @RouterOperation(path = "/api/v1/chassis/{id}",
                    beanClass = ChassisService.class, beanMethod = "getChassisItemById",
                    method = RequestMethod.GET,
                    operation = @Operation(operationId = "getChassisItem",
                            summary = "Get a chassis element by its Id",
                            description = "Find in the system database a chassis element with the supplied id",
                            parameters = @Parameter(in = ParameterIn.PATH,
                                    name = "id",
                                    description = "Chassis item ID"),
                            responses = {
                                @ApiResponse(responseCode = "200",
                                        description = "Chassis item found successfully",
                                        content = @Content(
                                                mediaType = APPLICATION_JSON_VALUE,
                                                schema = @Schema(implementation = Chassis.class))),
                                @ApiResponse(responseCode = "400",
                                        description = "Invalid chassis item ID number supplied",
                                        content = @Content(
                                                mediaType = APPLICATION_JSON_VALUE,
                                                schema = @Schema(implementation = ErrorResponse.class))),
                                @ApiResponse(responseCode = "404",
                                        description = "Chassis item with supplied ID not found",
                                        content = @Content)})),
            @RouterOperation(path = "/api/v1/chassis",
                    beanClass = ChassisService.class, beanMethod = "findAllChassisItems",
                    method = RequestMethod.GET,
                    operation = @Operation(operationId = "listChassisItems",
                            summary = "Get all chassis elements",
                            description = "Return all existing chassis elements from system database",
                            responses = @ApiResponse(responseCode = "200",
                                    description = "Chassis elements retrieved",
                                    content = @Content(
                                            mediaType = APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema =
                                                @Schema(implementation = Chassis.class)))))),
            @RouterOperation(path = "/api/v1/chassis",
                    beanClass = ChassisService.class, beanMethod = "addChassisItem",
                    method = RequestMethod.POST,
                    operation = @Operation(operationId = "createChassisItem",
                            summary = "Add a new chassis element",
                            description = "Add a new chassis element into system database",
                            requestBody = @RequestBody(required = true, content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Chassis.class, required = true))),
                            responses = @ApiResponse(responseCode = "201",
                                    description = "Chassis element added",
                                    content = @Content(
                                            mediaType = APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Chassis.class))))),
            @RouterOperation(path = "/api/v1/chassis/nameContain",
                    beanClass = ChassisService.class, beanMethod = "findAllChassisItemsByNameContaining",
                    method = RequestMethod.GET,
                    operation = @Operation(operationId = "listChassisItemsContainingName",
                            summary = "Get chassis elements containing the supplied value in its name",
                            description = "Return from system database all chassis elements containing the supplied value in its name",
                            parameters = @Parameter(in = ParameterIn.QUERY,
                                    name = "value", required = true,
                                    description = "String containing in the chassis item name to be searched"),
                            responses = @ApiResponse(responseCode = "200",
                                    description = "Chassis elements retrieved",
                                    content = @Content(
                                            mediaType = APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema =
                                                @Schema(implementation = Chassis.class)))))),
            @RouterOperation(path = "/api/v1/chassis/client/nameContain/{" + VAR_IN_USERNAME + "}",
                    beanClass = GitHubClient.class, beanMethod = "searchUsernameContaining",
                    method = RequestMethod.GET,
                    operation = @Operation(operationId = "getChassisClientResponse",
                            tags = "github-client",
                            summary = "Get GitHub username containing the supplied value in its name",
                            description = "Return from GitHub all username containing the supplied value in its name and with one or more repos",
                            parameters = @Parameter(in = ParameterIn.PATH,
                                    name = VAR_IN_USERNAME, required = true,
                                    description = "String containing in the GitHub username to be searched"),
                            responses = {
                                    @ApiResponse(responseCode = "200",
                                            description = "GitHub users retrieved",
                                            content = @Content(
                                                    mediaType = APPLICATION_JSON_VALUE,
                                                    schema = @Schema(example = "{\"total_count\":0,\"incomplete_results\":false,\"items\":[]}"))),
                                    @ApiResponse(responseCode = "404",
                                            description = "Username value not supplied",
                                            content = @Content)
                            }))
    })
    @Bean
    RouterFunction<ServerResponse> routes(ChassisRouteHandler handler) {
        return route().path("/api/v1/chassis", b1 -> b1
                        .path("/client", b2 -> b2
                                .GET("/nameContain/{" + VAR_IN_USERNAME + "}", handler::getChassisClientResponse)
                                .GET("/nameContain", handler::invalidClientNamePath))
                        .GET("/infinite", handler::infiniteStream)
                        .GET("/nameContain", handler::listChassisItemsContainingName)
                        .GET("/{id}", handler::getChassisItem)
                        .GET( handler::listChassisItems)
                        .POST(accept(APPLICATION_JSON), handler::createChassisItem))
                .filter(handler::errorHandlerFilter)
                .build();
    }

    @Bean
    ErrorProperties errorProperties() {
        ErrorProperties errorProperties = new ErrorProperties();
        errorProperties.setIncludeException(true);
        errorProperties.setIncludeMessage(IncludeAttribute.ALWAYS);
        errorProperties.setIncludeBindingErrors(IncludeAttribute.ALWAYS);
        errorProperties.setIncludeStacktrace(IncludeAttribute.ON_PARAM);

        return errorProperties;
    }
}
