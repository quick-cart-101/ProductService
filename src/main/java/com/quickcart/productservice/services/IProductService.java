package com.quickcart.productservice.services;

import com.quickcart.productservice.model.Product;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing products in the QuickCart system.
 * Provides methods for retrieving, creating, updating, and deleting products.
 */
public interface IProductService {

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param productId The unique identifier of the product.
     * @return The {@link Product} if found.
     */
    Product getProductById(UUID productId);

    /**
     * Retrieves all products available in the system.
     *
     * @return A list of all {@link Product} entities.
     */
    List<Product> getAllProducts();

    /**
     * Replaces an existing product with updated details.
     *
     * @param productId The unique identifier of the product to update.
     * @param request   The updated product details.
     * @return The updated {@link Product} entity.
     */
    Product replaceProduct(UUID productId, Product request);

    /**
     * Deletes a product by its unique identifier.
     *
     * @param productId The unique identifier of the product to delete.
     */
    void deleteProductById(UUID productId);

    /**
     * Saves a new product to the system.
     *
     * @param product The {@link Product} entity to be created.
     * @return The created {@link Product} entity.
     */
    Product saveProduct(Product product);

    /**
     * Retrieves all products that belong to a specific category.
     *
     * @param categoryId The unique identifier of the category.
     * @return A list of {@link Product} entities that belong to the specified category.
     */
    List<Product> getProductsByCategoryId(UUID categoryId);
}
