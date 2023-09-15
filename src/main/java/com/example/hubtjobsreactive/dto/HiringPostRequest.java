package com.example.hubtjobsreactive.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class HiringPostRequest {

    @NotBlank(message = "title cannot be blank")
    private String title;

    @NotBlank(message = "category cannot be blank")
    private String category;

    @NotBlank(message = "content cannot be blank")
    private String content;

    private String tag1;

    private String tag2;

    private String tag3;
}
