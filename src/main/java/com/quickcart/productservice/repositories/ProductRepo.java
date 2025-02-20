package com.quickcart.productservice.repositories;

import com.quickcart.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {
    List<Product> findByCategory_Id(UUID categoryId);
}
