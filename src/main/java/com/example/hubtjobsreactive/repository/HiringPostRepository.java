package com.example.hubtjobsreactive.repository;

import com.example.hubtjobsreactive.entity.HiringPost;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface HiringPostRepository extends ReactiveCrudRepository<HiringPost, Long> {
    @Query("SELECT * from hiring_post where lower(title) like lower(concat('%', :keyword, '%')) " +
            "or lower(category) like lower(concat('%', :keyword, '%')) " +
            "or lower(tag1) = lower(:keyword) " +
            "or lower(tag2) = lower(:keyword) " +
            "or lower(tag3) = lower(:keyword)")
    Flux<HiringPost> findByKeyword(@Param("keyword") String keyword);
}
