package com.neueda.microservice.reactive.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

// https://dzone.com/articles/lombok-and-jpa-what-may-go-wrong
@Getter
@Setter
public class ChassisEntity {

    @Id
    private Long id;
    private String name;
    private String description;
}

