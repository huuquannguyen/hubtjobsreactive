package com.example.hubtjobsreactive.controller;

import com.example.hubtjobsreactive.dto.HiringPostResponse;
import com.example.hubtjobsreactive.dto.HiringPostRequest;
import com.example.hubtjobsreactive.monitoring.MetricApplier;
import com.example.hubtjobsreactive.service.impl.HiringPostServiceImpl;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HiringPostController {

    private final HiringPostServiceImpl hiringPostService;

    private final MetricApplier<HiringPostResponse> metricApplier;

    @GetMapping("/posts/{id}")
    public Mono<HiringPostResponse> getPostById(@PathVariable Long id) {
        return metricApplier.applyMetrics(List.of(Tag.of("method", "GET"), Tag.of("uri", "/posts")),
                hiringPostService.getHiringPostById(id));
    }

    @GetMapping("/posts")
    public Flux<HiringPostResponse> searchPost(@RequestParam(name = "keyword", required = false) String keyword) {
        return metricApplier.applyMetrics(List.of(Tag.of("method", "GET"), Tag.of("uri", "/posts")),
                hiringPostService.searchPost(keyword));
    }

    @PostMapping(value = "poster/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<HiringPostResponse> createHiringPost(@Valid HiringPostRequest request,
                                                     @RequestPart(value = "image", required = false) FilePart filePart) {
        return metricApplier.applyMetrics(List.of(Tag.of("method", "POST"), Tag.of("uri", "/poster/posts")),
                hiringPostService.createHiringPost(request, filePart));
    }
}
