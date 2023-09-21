package com.example.hubtjobsreactive.event;

import com.example.hubtjobsreactive.service.HiringPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class HiringPostEventProducerConfig {

    private final HiringPostService hiringPostService;

    @Bean
    Supplier<Flux<Message<?>>> sendCreateHiringPostEvent() {
        return hiringPostService::getProducer;
    }
}
