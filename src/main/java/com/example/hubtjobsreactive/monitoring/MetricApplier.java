package com.example.hubtjobsreactive.monitoring;

import io.micrometer.core.instrument.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public interface MetricApplier<T> {

    Flux<T> applyMetrics(Iterable<Tag> tags, Flux<T> data);

    Mono<T> applyMetrics(Iterable<Tag> tags, Mono<T> data);

}
