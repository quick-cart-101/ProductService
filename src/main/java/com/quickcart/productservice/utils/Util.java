package com.quickcart.productservice.utils;

import com.quickcart.productservice.dto.CategoryDto;
import com.quickcart.productservice.dto.ProductDto;
import com.quickcart.productservice.entities.Category;
import com.quickcart.productservice.entities.Product;
import com.quickcart.productservice.entities.State;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static Product from(ProductDto productDto) {
        if (ObjectUtils.isEmpty(productDto)) {
            return null;
        }
        Product product = new Product();
        product.setState(State.ACTIVE);
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        product.setDescription(productDto.getDescription());

        if (!CollectionUtils.isEmpty(productDto.getCategory())) {
            List<Category> categoryList = productDto.getCategory().stream()
                    .map(dto -> Category.builder()
                            .id(dto.getId())
                            .name(dto.getName())
                            .description(dto.getDescription()).build()).collect(Collectors.toList());
            product.setCategory(categoryList);
        }
        return product;
    }

    public static ProductDto from(Product product) {
        if (ObjectUtils.isEmpty(product)) {
            return null;
        }
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());
        if (!CollectionUtils.isEmpty(product.getCategory())) {
            List<CategoryDto> categoryList = product.getCategory().stream()
                    .map(dto -> CategoryDto.builder()
                            .id(dto.getId())
                            .name(dto.getName())
                            .description(dto.getDescription()).build()).collect(Collectors.toList());
            productDto.setCategory(categoryList);
        }
        return productDto;
    }
}
