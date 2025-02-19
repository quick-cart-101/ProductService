package com.quickcart.productservice.controller;

import com.quickcart.productservice.dto.ProductDto;
import com.quickcart.productservice.exceptions.ProductNotFoundException;
import com.quickcart.productservice.model.Product;
import com.quickcart.productservice.services.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.quickcart.productservice.utils.Util.from;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = new ArrayList<>();
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            productDtos.add(from(product));
        }

        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable UUID productId) throws ProductNotFoundException {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(from(product));
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return from(productService.save(from(productDto)));
    }

    @PutMapping("/{productId}")
    public ProductDto replaceProduct(@PathVariable UUID productId, @RequestBody ProductDto request) throws ProductNotFoundException {
        Product productRequest = from(request);
        Product product = productService.replaceProduct(productId, productRequest);
        return from(product);
    }
}
