package com.example.hubtjobsreactive.monitoring.impl;

import com.example.hubtjobsreactive.monitoring.MetricApplier;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class RestApiMetricApplier<T> implements MetricApplier<T> {

    private final MeterRegistry meterRegistry;

    private final String COUNTER_METRIC_NAME = "counter-rest-api";

    private final String TIMER_METRIC_NAME = "timer-rest-api";

    @Value("${app.metrics.slo-request-duration}")
    private Duration[] metricRequestDurationSlos;


    @Override
    public Flux<T> applyMetrics(Iterable<Tag> tags, Flux<T> data) {
        AtomicReference<Instant> timer = new AtomicReference<>();
        return data.doOnSubscribe(p -> {
            timer.set(Instant.now());
            Counter.builder(COUNTER_METRIC_NAME)
                    .tags(tags)
                    .register(meterRegistry)
                    .increment(1);
        }).doOnComplete(() -> {
            Duration delta = Duration.between(timer.get(), Instant.now());
            Timer.builder(TIMER_METRIC_NAME)
                    .serviceLevelObjectives(metricRequestDurationSlos)
                    .tags(tags)
                    .register(meterRegistry)
                    .record(delta);
        });
    }

    @Override
    public Mono<T> applyMetrics(Iterable<Tag> tags, Mono<T> data) {
        AtomicReference<Instant> timer = new AtomicReference<>();
        return data.doOnSubscribe(p -> {
            timer.set(Instant.now());
            Counter.builder(COUNTER_METRIC_NAME)
                    .tags(tags)
                    .register(meterRegistry)
                    .increment(1);
        }).doOnSuccess(p -> {
            Duration delta = Duration.between(timer.get(), Instant.now());
            Timer.builder(TIMER_METRIC_NAME)
                    .serviceLevelObjectives(metricRequestDurationSlos)
                    .tags(tags)
                    .register(meterRegistry)
                    .record(delta);
        });
    }
}
