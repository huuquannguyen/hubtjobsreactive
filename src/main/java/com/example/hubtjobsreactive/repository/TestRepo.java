package com.example.hubtjobsreactive.repository;

import com.example.hubtjobsreactive.entity.TestEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TestRepo extends ReactiveCrudRepository<TestEntity, Long> {
}
