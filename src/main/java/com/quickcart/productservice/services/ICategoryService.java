package com.quickcart.productservice.services;

import com.quickcart.productservice.model.Category;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing product categories in the QuickCart system.
 * Provides methods for creating, retrieving, updating, and deleting categories.
 */
public interface ICategoryService {

    /**
     * Saves a new category to the system.
     *
     * @param category The {@link Category} entity to be created.
     * @return The created {@link Category} entity.
     */
    Category saveCategory(Category category);

    /**
     * Retrieves all categories available in the system.
     *
     * @return A list of all {@link Category} entities.
     */
    List<Category> getAllCategories();

    /**
     * Updates an existing category with new details.
     *
     * @param categoryId The unique identifier of the category to update.
     * @param category   The updated category details.
     * @return The updated {@link Category} entity.
     */
    Category updateCategory(UUID categoryId, Category category);

    /**
     * Deletes a category by its unique identifier.
     *
     * @param categoryId The unique identifier of the category to delete.
     */
    void deleteCategory(UUID categoryId);

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param categoryId The unique identifier of the category.
     * @return The {@link Category} entity if found.
     */
    Category getCategoryById(UUID categoryId);
}
