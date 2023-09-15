package com.example.hubtjobsreactive.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("hiring_post")
public class HiringPost {

    @Id
    private Long id;
    private String title;
    private String category;
    private String content;
    private int views;
    private String imgUrl;
    private String tag1;
    private String tag2;
    private String tag3;
    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
}
