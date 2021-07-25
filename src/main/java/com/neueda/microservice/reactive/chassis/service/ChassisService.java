package com.neueda.microservice.reactive.chassis.service;

import com.neueda.microservice.reactive.chassis.domain.ChassisEntity;
import com.neueda.microservice.reactive.chassis.model.Chassis;
import com.neueda.microservice.reactive.chassis.repository.ChassisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChassisService {

    private final ChassisRepository chassisRepository;

    public Flux<Chassis> getChassisItems() {
        return chassisRepository.findAll()
                .map(e -> new Chassis(e.getName(), e.getDescription()));
    }

    @Transactional
    public Mono<Chassis> addChassisItem(Chassis chassis) {
        ChassisEntity chassisEntity = new ChassisEntity();
        chassisEntity.setName(chassis.name());
        chassisEntity.setDescription(chassis.description());

        return chassisRepository.save(chassisEntity)
                .map(e -> new Chassis(e.getName(), e.getDescription()));
    }
}
