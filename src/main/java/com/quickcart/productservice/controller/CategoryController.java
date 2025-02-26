package com.quickcart.productservice.controller;

import com.quickcart.productservice.entities.Category;
import com.quickcart.productservice.services.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing product categories in the QuickCart system.
 * Provides endpoints for creating, retrieving, updating, and deleting categories.
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    /**
     * Constructs a new {@code CategoryController} with the given category service.
     *
     * @param categoryService The service responsible for category operations.
     */
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Creates a new category.
     *
     * @param category The category details to be created.
     * @return The created category wrapped in {@link ResponseEntity} with HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Category> saveCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.CREATED);
    }

    /**
     * Retrieves all available categories.
     *
     * @return A list of all categories wrapped in {@link ResponseEntity} with HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param categoryId The unique identifier of the category.
     * @return The category details wrapped in {@link ResponseEntity} with HTTP status 200 (OK).
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable UUID categoryId) {
        return new ResponseEntity<>(categoryService.getCategoryById(categoryId), HttpStatus.OK);
    }

    /**
     * Updates an existing category by its unique identifier.
     *
     * @param categoryId The unique identifier of the category to update.
     * @param category   The updated category details.
     * @return The updated category wrapped in {@link ResponseEntity} with HTTP status 202 (Accepted).
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID categoryId, @RequestBody Category category) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryId, category), HttpStatus.ACCEPTED);
    }

    /**
     * Deletes a category by its unique identifier.
     *
     * @param categoryId The unique identifier of the category to delete.
     * @return A success message wrapped in {@link ResponseEntity} with HTTP status 202 (Accepted).
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category with ID: " + categoryId + " has been deleted.", HttpStatus.ACCEPTED);
    }
}
