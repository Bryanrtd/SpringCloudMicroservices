package com.microservice.productservice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;

@jakarta.persistence.Entity
@Table(name = "tbl_categories")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
    @jakarta.persistence.Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
