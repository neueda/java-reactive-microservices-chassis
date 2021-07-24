package com.neueda.microservice.reactive.chassis.controller;

import com.neueda.microservice.reactive.chassis.model.User;
import com.neueda.microservice.reactive.chassis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequestMapping("/")
@RestController
public class WelcomeController {

    @Autowired
    private UserService userService;

    @GetMapping("hello")
    public String hello(@RequestParam(name = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }

    @PostMapping("users")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> create(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("users")
    public Flux<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
