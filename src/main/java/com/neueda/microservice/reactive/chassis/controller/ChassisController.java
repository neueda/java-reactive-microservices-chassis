package com.neueda.microservice.reactive.chassis.controller;

import com.neueda.microservice.reactive.chassis.client.GitHubClient;
import com.neueda.microservice.reactive.chassis.exception.IdFormatException;
import com.neueda.microservice.reactive.chassis.exception.MandatoryPathParameterException;
import com.neueda.microservice.reactive.chassis.model.Chassis;
import com.neueda.microservice.reactive.chassis.service.ChassisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

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

    @GetMapping("chassisSearch")
    public Flux<Chassis> getChassisByName(@RequestParam String name) {
        return chassisService.searchChassisByName(name);
    }

    @GetMapping({"chassisClient", "chassisClient/{username}"})
    public Mono<String> getChassisClientResponse(@PathVariable(required = false) String username) {
        if (StringUtils.hasText(username))
            return gitHubClient.searchUser(username);

        throw new MandatoryPathParameterException("chassisClient/{username}", "'username' is mandatory. Value: [" + username + "]");
    }

    @PostMapping("chassis")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Chassis> create(@Valid @RequestBody Chassis chassis) {
        return chassisService.addChassis(chassis);
    }
}
