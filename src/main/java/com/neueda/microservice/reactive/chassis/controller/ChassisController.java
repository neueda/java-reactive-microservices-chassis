package com.neueda.microservice.reactive.chassis.controller;

import com.neueda.microservice.reactive.chassis.model.Chassis;
import com.neueda.microservice.reactive.chassis.service.ChassisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ChassisController {

    private final ChassisService chassisService;

    @GetMapping("chassisList")
    public Flux<Chassis> getAllChassis() {
        return chassisService.getChassisItems();
    }

    @PostMapping("chassisItem")
//    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Chassis> create(@RequestBody @Valid Chassis chassis) {
        return chassisService.addChassisItem(chassis);
    }
}
