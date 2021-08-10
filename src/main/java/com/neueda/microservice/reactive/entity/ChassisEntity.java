package com.neueda.microservice.reactive.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChassisEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}

