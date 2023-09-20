package com.example.hubtjobsreactive;

import com.example.hubtjobsreactive.config.test.H2DbTestInitializer;
import com.example.hubtjobsreactive.dto.HiringPostResponse;
import com.example.hubtjobsreactive.repository.HiringPostRepository;
import com.example.hubtjobsreactive.security.WithMockJwt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest
@AutoConfigureWebTestClient
@Import({H2DbTestInitializer.class})
class HubtjobsReactiveApplicationTests {

    @Autowired
    WebTestClient testClient;

    @Autowired
    HiringPostRepository repository;

    @Test
    @WithMockUser
    public void testGetPostById() {
         var response = testClient.get()
                .uri("/posts/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(HiringPostResponse.class)
                .returnResult()
                .getResponseBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getTitle(), "job");
    }

    @Test
    @WithMockUser
    public void testGetAllPost() {
        testClient.get()
                .uri("/posts")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(HiringPostResponse.class)
                .hasSize(3);
    }

    @Test
    @WithMockJwt(roles = "ROLE_poster")
    public void testCreatePost() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("title", "job3");
        builder.part("category", "category3");
        builder.part("content", "hello3");
        builder.part("image", new FileSystemResource("src/test/resources/image/test_img.png"));
        var response = testClient.post()
                .uri("/poster/posts")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(HiringPostResponse.class)
                .returnResult()
                .getResponseBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getCreatedBy(), "test_user");
        repository.deleteById(response.getId());
    }

}
