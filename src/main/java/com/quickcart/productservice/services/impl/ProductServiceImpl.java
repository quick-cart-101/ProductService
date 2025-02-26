package com.quickcart.productservice.services.impl;

import com.quickcart.productservice.exceptions.CategoryNotFoundException;
import com.quickcart.productservice.exceptions.ProductNotFoundException;
import com.quickcart.productservice.entities.Category;
import com.quickcart.productservice.entities.Product;
import com.quickcart.productservice.repositories.CategoryRepo;
import com.quickcart.productservice.repositories.ProductRepo;
import com.quickcart.productservice.services.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.quickcart.productservice.utils.Constants.KEY_SEPARATOR;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final RedisTemplate<String, Product> redisTemplate;

    @Value("${PRODUCT_ID_CACHE_KEY}")
    private String productCacheKey;

    public ProductServiceImpl(ProductRepo productRepo, CategoryRepo categoryRepo, RedisTemplate<String, Product> redisTemplate) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public Product getProductById(UUID productId) throws ProductNotFoundException {
        String cacheKey = getCacheKey(productId.toString());

        Product cachedProduct = (Product) redisTemplate.opsForHash().get(cacheKey, productId.toString());
        if (!ObjectUtils.isEmpty(cachedProduct)) {
            log.info("Cache hit for product ID: {}", productId);
            return cachedProduct;
        }

        return productRepo.findById(productId)
                .map(product -> {
                    redisTemplate.opsForHash().put(cacheKey, product.getId().toString(), product);
                    return product;
                })
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", productId);
                    return new ProductNotFoundException("Product with ID: " + productId + " not available.");
                });
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> allProducts = productRepo.findAll();
        log.info("Fetched {} products", allProducts.size());
        return allProducts;
    }

    @Override
    public Product replaceProduct(UUID productId, Product product) {
        if (!productRepo.existsById(productId)) {
            log.error("Product with ID {} not found, failed to replace", productId);
            throw new ProductNotFoundException("Product with ID: " + productId + " not available.");
        }

        product.setId(productId);
        Product updatedProduct = productRepo.save(product);
        log.info("Product is updated with ID: {}", product);

        // Update cache
        redisTemplate.opsForHash().put(getCacheKey(productId.toString()), productId.toString(), updatedProduct);
        return updatedProduct;
    }

    @Override
    public void deleteProductById(UUID productId) {
        if (!productRepo.existsById(productId)) {
            log.error("Product with ID {} not found, cannot delete", productId);
            throw new ProductNotFoundException("Product with ID: " + productId + " not found.");
        }

        productRepo.deleteById(productId);
        log.info("Product is deleted with ID: {}", productId);
        redisTemplate.opsForHash().delete(getCacheKey(productId.toString()), productId);
    }


    @Override
    public Product saveProduct(Product product) {
        Objects.requireNonNull(product, "Product must not be null");
        Objects.requireNonNull(product.getCategory(), "Product category must not be null");

        List<Category> categories = product.getCategory().stream()
                .map(category -> categoryRepo.findById(category.getId())
                        .orElseThrow(() -> new CategoryNotFoundException("Category with ID: " + category.getId() + " not found")))
                .collect(Collectors.toList());

        product.setCategory(categories);
        return productRepo.save(product);
    }


    @Override
    public List<Product> getProductsByCategoryId(UUID categoryId) {
        return productRepo.findByCategory_Id(categoryId);
    }

    @Override
    public List<Product> getProductsByIds(List<UUID> productIds) {
        List<Product> products = productRepo.findAllById(productIds);
        // Check if any IDs were not found
        if (products.size() != productIds.size()) {
            List<UUID> foundIds = products.stream()
                    .map(Product::getId)
                    .toList();

            List<UUID> missingIds = productIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new ProductNotFoundException("Products not found for IDs: " + missingIds);
        }
        return products;
    }

    private String getCacheKey(String productId) {
        return productCacheKey + KEY_SEPARATOR + productId;
    }

}
