package com.quickcart.productservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private Double price;
    private List<CategoryDto> category;
}
