package com.neueda.microservice.reactive.controller;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.exception.IdFormatException;
import com.neueda.microservice.reactive.exception.MandatoryPathParameterException;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.model.ErrorResponse;
import com.neueda.microservice.reactive.service.ChassisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChassisController {

    private final ChassisService chassisService;
    private final GitHubClient gitHubClient;


    @Operation(tags = "chassis",
            summary = "Get all chassis elements",
            description = "Return all existing chassis elements from system database")
    @ApiResponses(@ApiResponse(responseCode = "200",
            description = "Chassis elements retrieved",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Chassis[].class))))
    @GetMapping("chassis")
    public Flux<Chassis> getAllChassis() {
        return chassisService.retrieveAllChassis();
    }

    @Operation(tags = "chassis",
            summary = "Add a new chassis element",
            description = "Add a new chassis element into system database")
    @ApiResponses(@ApiResponse(responseCode = "201",
            description = "Chassis element added",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Chassis.class))))
    @PostMapping("chassis")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Chassis> create(
            @Parameter(description = "Chassis element to be created")
            @Valid @RequestBody Chassis chassis) {
        return chassisService.addChassis(chassis);
    }

    @Operation(tags = "chassis",
            summary = "Get a chassis element by its id",
            description = "Find in the system database a chassis element with the supplied id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Chassis element found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Chassis.class))),
            @ApiResponse(responseCode = "400",
                    description = "Chassis element with supplied id not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "412",
                    description = "Chassis element id supplied is not a valid number",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("chassis/{id}")
    public Mono<Chassis> getChassisById(
            @Parameter(description = "Id containing in the GitHub username")
            @PathVariable String id) {

        try {
            return chassisService.searchChassisById(Long.valueOf(id));
        } catch(NumberFormatException ex) {
            throw new IdFormatException("/v1/chassis/" + id, ex);
        }
    }

    @Operation(tags = "chassis",
            summary = "Get chassis elements containing the supplied value in its name",
            description = "Return from system database all chassis elements containing the supplied value in its name")
    @ApiResponses(@ApiResponse(responseCode = "200",
            description = "Chassis elements retrieved",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Chassis[].class))))
    @GetMapping("chassisNameContain")
    public Flux<Chassis> getChassisByName(
            @Parameter(description = "String containing in the chassis element name to be searched")
            @RequestParam String value) {

        return chassisService.searchChassisByNameContaining(value);
    }

    @Operation(tags = "chassis-clients",
            summary = "Get GitHub username containing the supplied value in its name",
            description = "Return from GitHub all username containing the supplied value in its name and with one or more repos")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "GitHub users retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"total_count\":0,\"incomplete_results\":false,\"items\":[]}"))),
            @ApiResponse(responseCode = "404",
                    description = "Username value not supplied",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping({"chassisClientNameContain", "chassisClientNameContain/{usernamePart}"})
    public Mono<String> getChassisClientResponse(
            @Parameter(description = "String containing in the GitHub username to be searched")
            @PathVariable Optional<String> usernamePart) {

        return gitHubClient.searchUsernameContaining(
                usernamePart.orElseThrow(() ->
                        new MandatoryPathParameterException(
                                "chassisClientNameContain/{usernamePart}",
                                "'username' is mandatory")));
    }
}
