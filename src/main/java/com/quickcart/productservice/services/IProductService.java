package com.quickcart.productservice.services;

import com.quickcart.productservice.exceptions.ProductNotFoundException;
import com.quickcart.productservice.model.Product;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    Product getProductById(UUID productId) throws ProductNotFoundException;

    List<Product> getAllProducts();

    Product replaceProduct(UUID productId,Product request) throws ProductNotFoundException;

    Product save(Product product);
}
