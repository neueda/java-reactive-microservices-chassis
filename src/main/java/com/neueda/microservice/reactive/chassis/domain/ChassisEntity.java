package com.neueda.microservice.reactive.chassis.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class ChassisEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}

