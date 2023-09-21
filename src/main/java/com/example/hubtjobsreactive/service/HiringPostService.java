package com.example.hubtjobsreactive.service;

import com.example.hubtjobsreactive.dto.HiringPostRequest;
import com.example.hubtjobsreactive.dto.HiringPostResponse;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HiringPostService {

    Mono<HiringPostResponse> createHiringPost(HiringPostRequest request, FilePart filePart);

    Mono<HiringPostResponse> getHiringPostById(Long id);

    Flux<HiringPostResponse> getAllPost();

    Flux<HiringPostResponse> searchPost(String keyword);

    Flux<Message<?>> getProducer();

}
