package com.quickcart.productservice.services.impl;

import com.quickcart.productservice.exceptions.ProductNotFoundException;
import com.quickcart.productservice.model.Product;
import com.quickcart.productservice.repositories.ProductRepo;
import com.quickcart.productservice.services.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

import static com.quickcart.productservice.utils.Constants.KEY_SEPARATOR;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    private final ProductRepo productRepo;
    private final RedisTemplate<String, Product> redisTemplate;

    @Value("${PRODUCT_ID_CACHE_KEY}")
    private String productCacheKey;

    public ProductServiceImpl(ProductRepo productRepo, RedisTemplate<String, Product> redisTemplate) {
        this.productRepo = productRepo;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getProductById(UUID productId) throws ProductNotFoundException {
        String cacheKey = getCacheKey(productId);

        Product cachedProduct = (Product) redisTemplate.opsForHash().get(cacheKey, productId);
        if (!ObjectUtils.isEmpty(cachedProduct)) {
            log.info("Cache hit for product ID: {}", productId);
            return cachedProduct;
        }

        return productRepo.findById(productId)
                .map(product -> {
                    redisTemplate.opsForHash().put(cacheKey, product.getId(), product);
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
        redisTemplate.opsForHash().put(getCacheKey(productId), productId, updatedProduct);
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
        redisTemplate.opsForHash().delete(getCacheKey(productId), productId);
    }


    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    public List<Product> getProductsByCategoryId(UUID categoryId) {
        return productRepo.findByCategory_Id(categoryId);
    }

    private String getCacheKey(UUID productId) {
        return productCacheKey + KEY_SEPARATOR + productId;
    }

}
