package com.quickcart.productservice.controllers;

import com.quickcart.productservice.controller.CategoryController;
import com.quickcart.productservice.model.Category;
import com.quickcart.productservice.services.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void testSaveCategory() {
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Electronics");

        when(categoryService.saveCategory(category)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.saveCategory(category);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(category, response.getBody());
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

        when(categoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
    }

    @Test
    public void testGetCategoryById() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.getCategoryById(categoryId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    public void testUpdateCategory() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        when(categoryService.updateCategory(categoryId, category)).thenReturn(category);

        ResponseEntity<Category> response = categoryController.updateCategory(categoryId, category);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    public void testDeleteCategory() {
        UUID categoryId = UUID.randomUUID();

        ResponseEntity<String> response = categoryController.deleteCategory(categoryId);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Category with ID: " + categoryId + " has been deleted.", response.getBody());
    }
}