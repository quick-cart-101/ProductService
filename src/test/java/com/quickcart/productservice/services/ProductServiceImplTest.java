//package com.quickcart.productservice.services;
//
//import com.quickcart.productservice.exceptions.ProductNotFoundException;
//import com.quickcart.productservice.model.Category;
//import com.quickcart.productservice.model.Product;
//import com.quickcart.productservice.repositories.CategoryRepo;
//import com.quickcart.productservice.repositories.ProductRepo;
//import com.quickcart.productservice.services.impl.ProductServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProductServiceImplTest {
//
//    @Mock
//    private ProductRepo productRepo;
//
//    @Mock
//    private CategoryRepo categoryRepo;
//
//    @Mock
//    private RedisTemplate<String, Product> redisTemplate;
//
//    @Mock
//    private HashOperations<String, UUID, Product> hashOperations;
//
//    @InjectMocks
//    private ProductServiceImpl productService;
//
//    private Product product;
//    private UUID id = UUID.randomUUID();
//
//    @BeforeEach
//    void setUp() {
//        UUID categoryId = UUID.randomUUID();
//        Category category = Category.builder()
//                .id(categoryId)
//                .name("name")
//                .description("desc")
//                .build();
//
//        product = new Product();
//        product.setId(id);
//        product.setName("Test Product");
//        product.setCategory(List.of(category));
//        product.setPrice(100.0);
//
//        // Correct way to mock Redis operations
//        when(hashOperations.get(anyString(), any(UUID.class))).thenReturn(null);
//
//        // Mock Redis behavior for caching
//        when(hashOperations.get(anyString(), any(UUID.class))).thenReturn(null); // Simulate cache miss
//        doNothing().when(hashOperations).put(anyString(), any(UUID.class), any(Product.class));
//
//        // Mock category repo
//        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
//    }
//
//
//    @Test
//    void testGetAllProducts() {
//        when(productRepo.findAll()).thenReturn(Arrays.asList(product));
//        List<Product> products = productService.getAllProducts();
//        assertFalse(products.isEmpty());
//        assertEquals(1, products.size());
//        assertEquals("Test Product", products.get(0).getName());
//    }
//
//    @Test
//    void testGetProductById_CacheHit() {
//        when(hashOperations.get(anyString(), any(UUID.class))).thenReturn(product);
//        Product foundProduct = productService.getProductById(id);
//        assertNotNull(foundProduct);
//        assertEquals("Test Product", foundProduct.getName());
//        verify(productRepo, never()).findById(id); // Ensure DB is not queried if cache is hit
//    }
//
//    @Test
//    void testGetProductById_CacheMiss() {
//        when(hashOperations.get(anyString(), any(UUID.class))).thenReturn(null);
//        when(productRepo.findById(id)).thenReturn(Optional.of(product));
//
//        Product foundProduct = productService.getProductById(id);
//        assertNotNull(foundProduct);
//        assertEquals("Test Product", foundProduct.getName());
//        verify(productRepo, times(1)).findById(id);
//        verify(hashOperations, times(1)).put(anyString(), eq(id), eq(product)); // Cache is updated
//    }
//
//    @Test
//    void testGetProductById_ProductNotFound() {
//        when(hashOperations.get(anyString(), any(UUID.class))).thenReturn(null);
//        when(productRepo.findById(id)).thenReturn(Optional.empty());
//
//        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(id));
//    }
//
//    @Test
//    void testSaveProduct() {
//        when(productRepo.save(any(Product.class))).thenReturn(product);
//        Product savedProduct = productService.saveProduct(product);
//        assertNotNull(savedProduct);
//        assertEquals("Test Product", savedProduct.getName());
//    }
//
//    @Test
//    void testReplaceProduct() {
//        when(productRepo.existsById(id)).thenReturn(true);
//        when(productRepo.save(any(Product.class))).thenReturn(product);
//
//        Product updatedProduct = productService.replaceProduct(id, product);
//        assertNotNull(updatedProduct);
//        assertEquals("Test Product", updatedProduct.getName());
//        verify(hashOperations, times(1)).put(anyString(), eq(id), eq(updatedProduct));
//    }
//
//    @Test
//    void testDeleteProduct() {
//        when(productRepo.existsById(id)).thenReturn(true);
//        doNothing().when(productRepo).deleteById(id);
//
//        productService.deleteProductById(id);
//        verify(hashOperations, times(1)).delete(anyString(), eq(id));
//    }
//}
