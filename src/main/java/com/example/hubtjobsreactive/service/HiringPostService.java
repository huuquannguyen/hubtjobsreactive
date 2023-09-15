package com.example.hubtjobsreactive.service;

import com.example.hubtjobsreactive.dto.HiringPostResponse;
import com.example.hubtjobsreactive.entity.HiringPost;
import com.example.hubtjobsreactive.dto.HiringPostRequest;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HiringPostService {

    Mono<HiringPostResponse> createHiringPost(HiringPostRequest request, FilePart filePart);

    Mono<HiringPostResponse> getHiringPostById(Long id);

    Flux<HiringPostResponse> getAllPost();

    Flux<HiringPostResponse> searchPost(String keyword);

}
