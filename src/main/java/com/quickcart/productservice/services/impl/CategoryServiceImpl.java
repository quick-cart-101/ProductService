package com.quickcart.productservice.services.impl;

import com.quickcart.productservice.exceptions.CategoryNotFoundException;
import com.quickcart.productservice.entities.Category;
import com.quickcart.productservice.repositories.CategoryRepo;
import com.quickcart.productservice.services.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category updateCategory(UUID categoryId, Category category) {
        categoryRepo.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category with ID: " + categoryId + " not exists"));
        category.setId(categoryId);
        return categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(UUID categoryId) {
        boolean exists = categoryRepo.existsById(categoryId);
        if (!exists) {
            throw new CategoryNotFoundException("Category does not exist with ID: " + categoryId);
        }
        categoryRepo.deleteById(categoryId);
    }

    @Override
    public Category getCategoryById(UUID categoryId) {
        return categoryRepo.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category does not exist with ID: " + categoryId));
    }
}
