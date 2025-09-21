package com.example.test.repository;

import com.example.test.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByProductID(String productID);
}