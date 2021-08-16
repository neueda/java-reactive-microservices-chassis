package com.neueda.microservice.reactive.service;

import com.neueda.microservice.reactive.entity.ChassisEntity;
import com.neueda.microservice.reactive.exception.ItemNotFoundException;
import com.neueda.microservice.reactive.model.Chassis;
import com.neueda.microservice.reactive.repository.ChassisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;
import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
public class ChassisService {

    private final ChassisRepository chassisRepository;

    public Flux<ChassisEntity> findAllChassisItems() {
        return chassisRepository.findAll();
    }

    public Mono<ChassisEntity> getChassisItemById(Long id) {
        return chassisRepository.findById(id)
                .switchIfEmpty(error(
                        new ItemNotFoundException("No element with ID " + id + " could be found")));
    }

    public Flux<ChassisEntity> findAllChassisItemsByNameContaining(String value) {
        ChassisEntity chassisEntity = new ChassisEntity().setName(value);

        ExampleMatcher matcher = matching().withMatcher("name", contains());
        Example<ChassisEntity> example = Example.of(chassisEntity, matcher);

        return chassisRepository.findAll(example);
    }

    @Transactional
    public Mono<ChassisEntity> addChassisItem(Chassis chassis) {
        return chassisRepository.save(buildChassisEntity(chassis));
    }

    private ChassisEntity buildChassisEntity(Chassis chassis) {
        return new ChassisEntity()
                .setName(chassis.name())
                .setDescription(chassis.description());
    }
}
