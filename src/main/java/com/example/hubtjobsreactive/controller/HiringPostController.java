package com.example.hubtjobsreactive.controller;

import com.example.hubtjobsreactive.dto.HiringPostResponse;
import com.example.hubtjobsreactive.dto.HiringPostRequest;
import com.example.hubtjobsreactive.service.impl.HiringPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class HiringPostController {

    private final HiringPostServiceImpl hiringPostService;


    @PostMapping(value = "poster/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<HiringPostResponse> createHiringPost(@Valid HiringPostRequest request,
                                                     @RequestPart("image") FilePart filePart) {
        return hiringPostService.createHiringPost(request, filePart);
    }
}
