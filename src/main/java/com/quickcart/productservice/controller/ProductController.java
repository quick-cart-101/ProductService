package com.quickcart.productservice.controller;

import com.quickcart.productservice.dto.ProductDto;
import com.quickcart.productservice.model.Product;
import com.quickcart.productservice.services.IProductService;
import com.quickcart.productservice.utils.Util;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.quickcart.productservice.utils.Util.from;

/**
 * Controller for managing products in the QuickCart system.
 * Provides endpoints for retrieving, creating, updating, and deleting products.
 */
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final IProductService productService;

    /**
     * Retrieves all products available in the system.
     *
     * @return A list of all products as {@link ProductDto} wrapped in {@link ResponseEntity}.
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = products.stream().map(Util::from).toList();
        return ResponseEntity.ok(productDtos);
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param productId The unique identifier of the product.
     * @return The product details as {@link ProductDto} wrapped in {@link ResponseEntity}.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable UUID productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(from(product));
    }

    /**
     * Creates a new product.
     *
     * @param productDto The product details to be created.
     * @return The created product as {@link ProductDto} wrapped in {@link ResponseEntity} with HTTP status 201 (Created).
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(from(productService.save(from(productDto))), HttpStatus.CREATED);
    }

    /**
     * Updates an existing product by its unique identifier.
     *
     * @param productId The unique identifier of the product to update.
     * @param request   The updated product details.
     * @return The updated product as {@link ProductDto} wrapped in {@link ResponseEntity} with HTTP status 200 (OK).
     */
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> replaceProduct(@PathVariable UUID productId, @RequestBody ProductDto request) {
        Product product = productService.replaceProduct(productId, from(request));
        return new ResponseEntity<>(from(product), HttpStatus.OK);
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param productId The unique identifier of the product to delete.
     * @return A success message wrapped in {@link ResponseEntity} with HTTP status 204 (No Content).
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product with ID: " + productId + " successfully deleted.");
    }
}
