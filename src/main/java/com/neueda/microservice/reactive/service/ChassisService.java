package com.neueda.microservice.reactive.service;

import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.exception.EntityNotFoundException;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.repository.ChassisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
@RequiredArgsConstructor
public class ChassisService {

    private final ChassisRepository chassisRepository;

    private final Function<ChassisEntity, Chassis> toViewModel =
            e -> new Chassis(e.getName(), e.getDescription());

    public Flux<Chassis> retrieveAllChassis() {
        return chassisRepository.findAll().map(toViewModel);
    }

    public Mono<Chassis> getChassisById(Long id) {
        return chassisRepository.findById(id)
                .map(toViewModel)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        "/v1/chassis/" + id,
                        "No element with ID " + id + " could be found")));
    }

    public Flux<Chassis> searchChassisByNameContaining(String value) {
        ChassisEntity chassisEntity =
                ChassisEntity.builder()
                        .name(value)
                        .build();

        ExampleMatcher matcher = matching().withMatcher("name", contains());
        Example<ChassisEntity> example = Example.of(chassisEntity, matcher);

        return chassisRepository.findAll(example).map(toViewModel);
    }

    @Transactional
    public Mono<Chassis> addChassis(Chassis chassis) {
        ChassisEntity chassisEntity = new ChassisEntity();
        chassisEntity.setName(chassis.name());
        chassisEntity.setDescription(chassis.description());

        return chassisRepository.save(chassisEntity).map(toViewModel);
    }
}
