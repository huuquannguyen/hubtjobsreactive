package com.example.hubtjobsreactive.service.impl;

import com.example.hubtjobsreactive.dto.HiringPostResponse;
import com.example.hubtjobsreactive.entity.HiringPost;
import com.example.hubtjobsreactive.dto.HiringPostRequest;
import com.example.hubtjobsreactive.repository.HiringPostRepository;
import com.example.hubtjobsreactive.service.HiringPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HiringPostServiceImpl implements HiringPostService {

    private final HiringPostRepository hiringPostRepository;

    @Value("${file-upload.post-image}")
    private String postImageDirectory;

    @Override
    public Mono<HiringPostResponse> createHiringPost(HiringPostRequest request, FilePart filePart) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(Jwt.class::cast)
                .map(Jwt::getSubject)
                .zipWhen(t -> {
                    HiringPost post = new HiringPost();
                    post.setCategory(request.getCategory());
                    post.setContent(request.getContent());
                    post.setTag1(request.getTag1());
                    post.setTag2(request.getTag2());
                    post.setTag3(request.getTag3());
                    post.setTitle(request.getTitle());
                    post.setViews(0);
                    post.setCreatedBy(t);
                    post.setCreatedDate(LocalDateTime.now());
                    return Mono.just(post);
                })
                .zipWhen(t -> uploadFile(filePart, t.getT1()))
                .flatMap(t -> {
                    t.getT1().getT2().setImgUrl(t.getT2());
                    return hiringPostRepository.save(t.getT1().getT2());
                })
                .map(r -> {
                    HiringPostResponse response = new HiringPostResponse();
                    BeanUtils.copyProperties(r, response);
                    return response;
                });
    }

    public Mono<String> uploadFile(FilePart filePartMono, String createdBy) {
        Path path = Paths.get(postImageDirectory);
        long nowEpoch = Instant.now().getEpochSecond();
        String newImgName = createdBy + "_" + nowEpoch;
        String fileName = filePartMono.filename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        Path absolutePath = path.resolve(newImgName + extension).toAbsolutePath();
        return filePartMono.transferTo(absolutePath).then(Mono.just(absolutePath.toString()));
    }
}