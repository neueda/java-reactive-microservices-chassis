package com.neueda.microservice.reactive.chassis.controller;

import com.neueda.microservice.reactive.chassis.exception.IdFormatException;
import com.neueda.microservice.reactive.chassis.model.Chassis;
import com.neueda.microservice.reactive.chassis.service.ChassisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ChassisController {

    private final ChassisService chassisService;

    @GetMapping("chassis")
    public Flux<Chassis> getAllChassis() {
        return chassisService.retrieveAllChassis();
    }

    @GetMapping("chassis/{id}")
    public Mono<Chassis> getChassisById(@PathVariable String id) {
        try {
            return chassisService.searchChassisById(Long.valueOf(id));
        } catch(NumberFormatException e) {
            throw new IdFormatException(e, "/v1/chassis/" + id);
        }
    }

    @GetMapping("chassisSearch")
    public Flux<Chassis> getChassisByName(@RequestParam String name) {
        return chassisService.searchChassisByName(name);
    }

    @PostMapping("chassis")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Chassis> create(@Valid @RequestBody Chassis chassis) {
        return chassisService.addChassis(chassis);
    }
}
