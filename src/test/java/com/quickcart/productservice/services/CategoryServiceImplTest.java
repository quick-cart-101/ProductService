package com.quickcart.productservice.services.impl;

import com.quickcart.productservice.entities.Category;
import com.quickcart.productservice.exceptions.CategoryNotFoundException;
import com.quickcart.productservice.repositories.CategoryRepo;
import com.quickcart.productservice.services.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private UUID categoryId;

    @BeforeEach
    public void setUp() {
        categoryId = UUID.randomUUID();
        category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");
    }

    @Test
    public void testSaveCategory() {
        when(categoryRepo.save(category)).thenReturn(category);

        Category savedCategory = categoryService.saveCategory(category);
        assertEquals(category, savedCategory);
    }

    @Test
    public void testGetAllCategories() {
        Category category2 = new Category();
        category2.setId(UUID.randomUUID());
        category2.setName("Books");

        List<Category> categories = Arrays.asList(category, category2);

        when(categoryRepo.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();
        assertEquals(categories, result);
    }

    @Test
    public void testUpdateCategory() {
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepo.save(category)).thenReturn(category);

        Category updatedCategory = categoryService.updateCategory(categoryId, category);
        assertEquals(category, updatedCategory);
    }

    @Test
    public void testUpdateCategoryNotFound() {
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(categoryId, category));
    }

    @Test
    public void testDeleteCategory() {
        when(categoryRepo.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepo).deleteById(categoryId);

        categoryService.deleteCategory(categoryId);
        verify(categoryRepo, times(1)).deleteById(categoryId);
    }

    @Test
    public void testDeleteCategoryNotFound() {
        when(categoryRepo.existsById(categoryId)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
    }

    @Test
    public void testGetCategoryById() {
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(categoryId);
        assertEquals(category, result);
    }

    @Test
    public void testGetCategoryByIdNotFound() {
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
    }
}