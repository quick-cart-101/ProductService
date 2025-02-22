package com.quickcart.productservice.services;

import com.quickcart.productservice.exceptions.CategoryNotFoundException;
import com.quickcart.productservice.model.Category;
import com.quickcart.productservice.repositories.CategoryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepo categoryRepo;


    private ICategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCategory() {
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Electronics");

        when(categoryRepo.save(category)).thenReturn(category);

        Category savedCategory = categoryService.saveCategory(category);
        assertEquals(category, savedCategory);
    }

    @Test
    public void testGetAllCategories() {
        Category category1 = new Category();
        category1.setId(UUID.randomUUID());
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setId(UUID.randomUUID());
        category2.setName("Books");

        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepo.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();
        assertEquals(categories, result);
    }

    @Test
    public void testUpdateCategory() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepo.save(category)).thenReturn(category);

        Category updatedCategory = categoryService.updateCategory(categoryId, category);
        assertEquals(category, updatedCategory);
    }

    @Test
    public void testUpdateCategoryNotFound() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(categoryId, category));
    }

    @Test
    public void testDeleteCategory() {
        UUID categoryId = UUID.randomUUID();

        when(categoryRepo.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepo).deleteById(categoryId);

        categoryService.deleteCategory(categoryId);
        verify(categoryRepo, times(1)).deleteById(categoryId);
    }

    @Test
    public void testDeleteCategoryNotFound() {
        UUID categoryId = UUID.randomUUID();

        when(categoryRepo.existsById(categoryId)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
    }

    @Test
    public void testGetCategoryById() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(categoryId);
        assertEquals(category, result);
    }

    @Test
    public void testGetCategoryByIdNotFound() {
        UUID categoryId = UUID.randomUUID();

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
    }
}