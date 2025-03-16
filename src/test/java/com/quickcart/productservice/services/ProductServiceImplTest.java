package com.quickcart.productservice.services;

import com.quickcart.productservice.entities.Category;
import com.quickcart.productservice.entities.Product;
import com.quickcart.productservice.exceptions.CategoryNotFoundException;
import com.quickcart.productservice.exceptions.ProductNotFoundException;
import com.quickcart.productservice.repositories.CategoryRepo;
import com.quickcart.productservice.repositories.ProductRepo;
import com.quickcart.productservice.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private RedisTemplate<String, Product> redisTemplate;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock HashOperations
        HashOperations hashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    @Test
    void testGetProductById_CacheHit() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);

        String cacheKey = "product:" + productId;

        when(redisTemplate.opsForHash().get(cacheKey, productId.toString())).thenReturn(product);

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(redisTemplate, times(1)).opsForHash().get(cacheKey, productId.toString());
        verify(productRepo, never()).findById(any());
    }

    @Test
    void testGetProductById_CacheMiss() throws ProductNotFoundException {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);

        String cacheKey = "product:" + productId;

        when(redisTemplate.opsForHash().get(cacheKey, productId.toString())).thenReturn(null);
        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(redisTemplate, times(3)).opsForHash().get(cacheKey, productId.toString());
        verify(productRepo, times(1)).findById(productId);
        verify(redisTemplate, times(1)).opsForHash().put(cacheKey, productId.toString(), product);
    }

    @Test
    void testGetProductById_ProductNotFound() {
        UUID productId = UUID.randomUUID();
        String cacheKey = "product:" + productId;

        when(redisTemplate.opsForHash().get(cacheKey, productId.toString())).thenReturn(null);
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));

        verify(redisTemplate, times(1)).opsForHash().get(cacheKey, productId.toString());
        verify(productRepo, times(1)).findById(productId);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productRepo.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepo, times(1)).findAll();
    }

    @Test
    void testReplaceProduct() {
        UUID productId = UUID.randomUUID();
        Product existingProduct = new Product();
        existingProduct.setId(productId);

        Product newProduct = new Product();
        newProduct.setId(productId);
        newProduct.setName("New Product");

        when(productRepo.existsById(productId)).thenReturn(true);
        when(productRepo.save(newProduct)).thenReturn(newProduct);

        Product result = productService.replaceProduct(productId, newProduct);

        assertNotNull(result);
        assertEquals("New Product", result.getName());
        verify(productRepo, times(1)).existsById(productId);
        verify(productRepo, times(1)).save(newProduct);
        verify(redisTemplate, times(1)).opsForHash().put("product:" + productId, productId.toString(), newProduct);
    }

    @Test
    void testReplaceProduct_ProductNotFound() {
        UUID productId = UUID.randomUUID();
        Product newProduct = new Product();
        newProduct.setId(productId);

        when(productRepo.existsById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.replaceProduct(productId, newProduct));

        verify(productRepo, times(1)).existsById(productId);
        verify(productRepo, never()).save(any());
        verify(redisTemplate, never()).opsForHash().put(anyString(), anyString(), any());
    }

    @Test
    void testDeleteProductById() {
        UUID productId = UUID.randomUUID();
        when(productRepo.existsById(productId)).thenReturn(true);

        productService.deleteProductById(productId);

        verify(productRepo, times(1)).existsById(productId);
        verify(productRepo, times(1)).deleteById(productId);
        verify(redisTemplate, times(1)).opsForHash().delete("product:" + productId, productId.toString());
    }

    @Test
    void testDeleteProductById_ProductNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepo.existsById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(productId));

        verify(productRepo, times(1)).existsById(productId);
        verify(productRepo, never()).deleteById(any());
        verify(redisTemplate, never()).opsForHash().delete(anyString(), anyString());
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setName("Test Product");

        Category category = new Category();
        category.setId(UUID.randomUUID());
        product.setCategory(Collections.singletonList(category));

        when(categoryRepo.findById(category.getId())).thenReturn(Optional.of(category));
        when(productRepo.save(product)).thenReturn(product);

        Product result = productService.saveProduct(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(categoryRepo, times(1)).findById(category.getId());
        verify(productRepo, times(1)).save(product);
    }

    @Test
    void testSaveProduct_CategoryNotFound() {
        Product product = new Product();
        product.setName("Test Product");

        Category category = new Category();
        category.setId(UUID.randomUUID());
        product.setCategory(Collections.singletonList(category));

        when(categoryRepo.findById(category.getId())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.saveProduct(product));

        verify(categoryRepo, times(1)).findById(category.getId());
        verify(productRepo, never()).save(any());
    }

    @Test
    void testGetProductsByCategoryId() {
        UUID categoryId = UUID.randomUUID();
        List<Product> products = Arrays.asList(new Product(), new Product());

        when(productRepo.findByCategory_Id(categoryId)).thenReturn(products);

        List<Product> result = productService.getProductsByCategoryId(categoryId);

        assertEquals(2, result.size());
        verify(productRepo, times(1)).findByCategory_Id(categoryId);
    }

    @Test
    void testGetProductsByIds() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        List<UUID> productIds = Arrays.asList(productId1, productId2);

        Product product1 = new Product();
        product1.setId(productId1);
        Product product2 = new Product();
        product2.setId(productId2);
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepo.findAllById(productIds)).thenReturn(products);

        List<Product> result = productService.getProductsByIds(productIds);

        assertEquals(2, result.size());
        verify(productRepo, times(1)).findAllById(productIds);
    }

    @Test
    void testGetProductsByIds_ProductNotFound() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        List<UUID> productIds = Arrays.asList(productId1, productId2);

        Product product1 = new Product();
        product1.setId(productId1);
        List<Product> products = Collections.singletonList(product1);

        when(productRepo.findAllById(productIds)).thenReturn(products);

        assertThrows(ProductNotFoundException.class, () -> productService.getProductsByIds(productIds));

        verify(productRepo, times(1)).findAllById(productIds);
    }
}