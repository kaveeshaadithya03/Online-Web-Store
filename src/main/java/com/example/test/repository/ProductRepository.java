package com.example.test.repository;

import com.example.test.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByProductID(String productID);
    List<Product> findBySellerId(String sellerId);
}