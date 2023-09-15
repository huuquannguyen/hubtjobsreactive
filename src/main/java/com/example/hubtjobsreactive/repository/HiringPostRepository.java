package com.example.hubtjobsreactive.repository;

import com.example.hubtjobsreactive.entity.HiringPost;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface HiringPostRepository extends ReactiveCrudRepository<HiringPost, Long> {
}
