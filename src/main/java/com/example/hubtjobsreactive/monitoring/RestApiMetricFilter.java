package com.example.hubtjobsreactive.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.reactivestreams.Publisher;
import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTagsProvider;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class RestApiMetricFilter implements WebFilter {

    private final String COUNTER_METRIC_NAME = "counter-rest-api";
    private final String TIMER_METRIC_NAME = "timer-rest-api";

    private final MeterRegistry registry;
    private final WebFluxTagsProvider tagsProvider;
    private final Duration[] metricRequestDurationSlo;

    public RestApiMetricFilter (MeterRegistry registry, WebFluxTagsProvider tagsProvider, Duration[] metricRequestDurationSlo) {
        this.registry = registry;
        this.tagsProvider = tagsProvider;
        this.metricRequestDurationSlo = metricRequestDurationSlo;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        RequestPath path = serverWebExchange.getRequest().getPath();
        PathPattern pattern = new PathPatternParser().parse("/actuator/**");
        if (pattern.matches(path.pathWithinApplication())) {
            return webFilterChain.filter(serverWebExchange);
        }
        return webFilterChain.filter(serverWebExchange).transformDeferred((call) -> this.filter(serverWebExchange, call));
    }

    private Publisher<Void> filter(ServerWebExchange exchange, Mono<Void> call) {
        long start = System.nanoTime();
        return call.doOnSuccess((done) -> {
            this.onSuccess(exchange, start);
        }).doOnError((cause) -> this.onError(exchange, start, cause));
    }

    private void onSuccess(ServerWebExchange exchange, long start) {
        this.count(exchange, null);
        this.record(exchange, start, null);
    }

    private void onError(ServerWebExchange exchange, long start, Throwable cause) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            this.count(exchange, cause);
            this.record(exchange, start, cause);
        } else {
            response.beforeCommit(() -> {
                this.count(exchange, cause);
                this.record(exchange, start, cause);
                return Mono.empty();
            });
        }

    }

    private void record(ServerWebExchange exchange, long start, Throwable cause) {
        Iterable<Tag> tags = this.tagsProvider.httpRequestTags(exchange, cause);
        Timer.builder(TIMER_METRIC_NAME)
                .tags(tags)
                .serviceLevelObjectives(this.metricRequestDurationSlo)
                .register(this.registry)
                .record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    }

    private void count(ServerWebExchange exchange, Throwable cause) {
        Iterable<Tag> tags = this.tagsProvider.httpRequestTags(exchange, cause);
        Counter.builder(COUNTER_METRIC_NAME)
                .tags(tags)
                .register(this.registry)
                .increment(1);
    }

}
