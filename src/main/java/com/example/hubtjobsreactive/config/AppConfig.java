package com.example.hubtjobsreactive.config;

import com.example.hubtjobsreactive.monitoring.RestApiMetricFilter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTagsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${app.metrics.slo-request-duration}")
    private Duration[] metricRequestDurationSlo;

    @Bean
    public RestApiMetricFilter restApiMetricFilter (MeterRegistry registry, WebFluxTagsProvider tagsProvider) {
        return new RestApiMetricFilter(registry, tagsProvider, metricRequestDurationSlo);
    }

}
