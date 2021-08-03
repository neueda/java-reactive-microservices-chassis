package com.neueda.microservice.reactive.controller;

import com.neueda.microservice.reactive.client.GitHubClient;
import com.neueda.microservice.reactive.exception.IdFormatException;
import com.neueda.microservice.reactive.exception.MandatoryPathParameterException;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.service.ChassisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ChassisController {

    private final ChassisService chassisService;
    private final GitHubClient gitHubClient;


    @GetMapping("chassis")
    public Flux<Chassis> getAllChassis() {
        return chassisService.retrieveAllChassis();
    }

    @GetMapping("chassis/{id}")
    public Mono<Chassis> getChassisById(@PathVariable String id) {
        try {
            return chassisService.searchChassisById(Long.valueOf(id));
        } catch(NumberFormatException ex) {
            throw new IdFormatException("/v1/chassis/" + id, ex);
        }
    }

    @GetMapping("chassisNameContain")
    public Flux<Chassis> getChassisByName(@RequestParam String value) {
        return chassisService.searchChassisByNameContaining(value);
    }

    @GetMapping({"chassisClientNameContain", "chassisClientNameContain/{usernamePart}"})
    public Mono<String> getChassisClientResponse(@PathVariable Optional<String> usernamePart) {
        String value = usernamePart.orElseThrow(() ->
                new MandatoryPathParameterException("chassisClientNameContain/{usernamePart}", "'username' is mandatory"));

        return gitHubClient.searchUsernameContaining(value);
    }

    @PostMapping("chassis")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Chassis> create(@Valid @RequestBody Chassis chassis) {
        return chassisService.addChassis(chassis);
    }
}
