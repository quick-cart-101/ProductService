package com.quickcart.productservice.controller;

import com.quickcart.productservice.dto.ProductDto;
import com.quickcart.productservice.model.Product;
import com.quickcart.productservice.services.IProductService;
import com.quickcart.productservice.utils.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.quickcart.productservice.utils.Util.from;

/**
 * Controller for managing products in the QuickCart system.
 * Provides endpoints for retrieving, creating, updating, and deleting products.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products available in the system.
     *
     * @return A {@link ResponseEntity} containing a list of all available products as {@link ProductDto}.
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = products.stream().map(Util::from).toList();
        return ResponseEntity.ok(productDtos);
    }

    /**
     * Retrieves products by their unique identifiers.
     *
     * @param productIds A list of unique identifiers (UUIDs) of the products to retrieve.
     * @return A {@link ResponseEntity} containing a list of products as {@link ProductDto}.
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<ProductDto>> getProductsByProductIds(@RequestBody List<UUID> productIds) {
        List<Product> products = productService.getProductsByIds(productIds);
        List<ProductDto> productDtos = products.stream().map(Util::from).toList();
        return ResponseEntity.ok(productDtos);
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param productId The unique identifier (UUID) of the product.
     * @return A {@link ResponseEntity} containing the product details as a {@link ProductDto}.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable UUID productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(from(product));
    }

    /**
     * Creates a new product.
     * Only accessible to users with the "ADMIN" role.
     *
     * @param productDto The product details to be created, received as a request body.
     * @return A {@link ResponseEntity} containing the created product as a {@link ProductDto},
     * along with an HTTP status of 201 (Created).
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(from(productService.saveProduct(from(productDto))), HttpStatus.CREATED);
    }

    /**
     * Updates an existing product by its unique identifier.
     * Only accessible to users with the "ADMIN" role.
     *
     * @param productId The unique identifier (UUID) of the product to update.
     * @param request   The updated product details received as a request body.
     * @return A {@link ResponseEntity} containing the updated product as a {@link ProductDto},
     * along with an HTTP status of 200 (OK).
     */
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> replaceProduct(@PathVariable UUID productId, @RequestBody ProductDto request) {
        Product product = productService.replaceProduct(productId, from(request));
        return new ResponseEntity<>(from(product), HttpStatus.OK);
    }

    /**
     * Deletes a product by its unique identifier.
     * Only accessible to users with the "ADMIN" role.
     *
     * @param productId The unique identifier (UUID) of the product to delete.
     * @return A {@link ResponseEntity} containing a success message along with an HTTP status of 204 (No Content).
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product with ID: " + productId + " successfully deleted.");
    }

    /**
     * Retrieves all products that belong to a specific category.
     *
     * @param categoryId The unique identifier (UUID) of the category.
     * @return A {@link ResponseEntity} containing a list of products belonging to the specified category as {@link ProductDto}.
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryId(@PathVariable UUID categoryId) {
        List<Product> products = productService.getProductsByCategoryId(categoryId);
        List<ProductDto> productDtos = products.stream().map(Util::from).toList();
        return ResponseEntity.ok(productDtos);
    }
}
