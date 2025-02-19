package com.quickcart.productservice.services.impl;

import com.quickcart.productservice.exceptions.ProductNotFoundException;
import com.quickcart.productservice.model.Product;
import com.quickcart.productservice.repositories.ProductRepo;
import com.quickcart.productservice.services.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    private final ProductRepo productRepo;

    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Product getProductById(UUID productId) throws ProductNotFoundException {
        Optional<Product> product = productRepo.findById(productId);
        if(product.isEmpty()) {
            log.error("Product with product ID: {} not found", productId);
            throw new ProductNotFoundException("Product with ID: "+productId+" not available.");
        }
        return product.get();
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> all = productRepo.findAll();
        System.out.println(all);
        return all;
    }

    @Override
    public Product replaceProduct(UUID productId, Product product) throws ProductNotFoundException {
        boolean productExists = productRepo.existsById(productId);
        if(!productExists) {
            log.error("Product with product ID: {} not found, failed to replace product: {}", productId, product);
            throw new ProductNotFoundException("Product with ID: "+productId+" not available.");
        }
        product.setId(productId);
        return productRepo.save(product);
    }

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }
}
