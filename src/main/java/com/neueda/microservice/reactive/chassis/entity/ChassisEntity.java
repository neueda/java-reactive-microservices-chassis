package com.neueda.microservice.reactive.chassis.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.lang.NonNull;

@Data
@Builder
@NoArgsConstructor
public class ChassisEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}

