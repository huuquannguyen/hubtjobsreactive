package com.example.hubtjobsreactive.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("test_table")
public class TestEntity {
    @Id
    private Long id;

    private String name;
}
