package com.quickcart.productservice.controllers;

import com.quickcart.productservice.controller.ProductController;
import com.quickcart.productservice.dto.ProductDto;
import com.quickcart.productservice.model.Product;
import com.quickcart.productservice.services.IProductService;
import com.quickcart.productservice.utils.Util;
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

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setName("Product 2");

        List<Product> products = Arrays.asList(product1, product2);
        List<ProductDto> productDtos = products.stream().map(Util::from).toList();

        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<ProductDto>> response = productController.getAllProducts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDtos, response.getBody());
    }

    @Test
    public void testFindProductById() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Product 1");

        when(productService.getProductById(productId)).thenReturn(product);

        ResponseEntity<ProductDto> response = productController.findProductById(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Util.from(product), response.getBody());
    }

    @Test
    public void testGetProductsByProductIds() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        List<UUID> productIds = Arrays.asList(productId1, productId2);

        Product product1 = new Product();
        product1.setId(productId1);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(productId2);
        product2.setName("Product 2");

        List<Product> products = Arrays.asList(product1, product2);
        List<ProductDto> productDtos = products.stream().map(Util::from).toList();

        when(productService.getProductsByIds(productIds)).thenReturn(products);

        ResponseEntity<List<ProductDto>> response = productController.getProductsByProductIds(productIds);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDtos, response.getBody());
    }

    @Test
    public void testCreateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Product 1");

        Product product = Util.from(productDto);
        product.setId(UUID.randomUUID());

        when(productService.saveProduct(product)).thenReturn(product);

        ResponseEntity<ProductDto> response = productController.createProduct(productDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Util.from(product), response.getBody());
    }

    @Test
    public void testReplaceProduct() {
        UUID productId = UUID.randomUUID();
        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");

        Product product = Util.from(productDto);
        product.setId(productId);

        when(productService.replaceProduct(productId, product)).thenReturn(product);

        ResponseEntity<ProductDto> response = productController.replaceProduct(productId, productDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Util.from(product), response.getBody());
    }

    @Test
    public void testDeleteProduct() {
        UUID productId = UUID.randomUUID();

        ResponseEntity<String> response = productController.deleteProduct(productId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Product with ID: " + productId + " successfully deleted.", response.getBody());
    }

    @Test
    public void testGetProductsByCategoryId() {
        UUID categoryId = UUID.randomUUID();
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setName("Product 2");

        List<Product> products = Arrays.asList(product1, product2);
        List<ProductDto> productDtos = products.stream().map(Util::from).toList();

        when(productService.getProductsByCategoryId(categoryId)).thenReturn(products);

        ResponseEntity<List<ProductDto>> response = productController.getProductsByCategoryId(categoryId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDtos, response.getBody());
    }
}