package com.neueda.microservice.reactive.chassi.service;

import com.neueda.microservice.reactive.chassi.repository.UserRepository;
import com.neueda.microservice.reactive.chassi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Mono<User> createUser(User user){
        return userRepository.save(user);
    }

    public Flux<User> getAllUsers(){
        return userRepository.findAll();
    }
}
