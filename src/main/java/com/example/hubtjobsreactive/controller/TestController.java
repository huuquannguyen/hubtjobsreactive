package com.example.hubtjobsreactive.controller;

import com.example.hubtjobsreactive.entity.TestEntity;
import com.example.hubtjobsreactive.repository.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

    @Autowired
    private TestRepo repo;

    @GetMapping("/users/{id}")
    public Mono<TestEntity> getUserInfo(@PathVariable Long id) {
        return repo.findById(id);
    }
}
