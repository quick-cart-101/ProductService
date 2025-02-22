package com.quickcart.productservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class CategoryDto {
    private UUID id;
    private String name;
    private String description;
}
