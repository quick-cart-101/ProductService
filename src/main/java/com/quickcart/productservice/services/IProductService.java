package com.quickcart.productservice.services;

import com.quickcart.productservice.exceptions.ProductNotFoundException;
import com.quickcart.productservice.model.Product;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    Product getProductById(UUID productId);

    List<Product> getAllProducts();

    Product replaceProduct(UUID productId, Product request);

    void deleteProductById(UUID productId);

    Product save(Product product);
}
