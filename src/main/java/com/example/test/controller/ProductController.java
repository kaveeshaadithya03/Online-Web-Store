package com.example.test.controller;

import com.example.test.model.Product;
import com.example.test.model.User;
import com.example.test.repository.ProductRepository;
import com.example.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Helper method to get authenticated seller's ID
    private String getAuthenticatedSellerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
            if (user != null && "seller".equals(user.getRole())) {
                return user.getId();
            }
        }
        throw new SecurityException("Unauthorized access: No authenticated seller found");
    }

    @GetMapping("/public")
    public ResponseEntity<List<Product>> getAllPublicProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Product> getPublicProductById(@PathVariable String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        String sellerId = getAuthenticatedSellerId();
        List<Product> products = productRepository.findBySellerId(sellerId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        String sellerId = getAuthenticatedSellerId();
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (!product.getSellerId().equals(sellerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @Valid @RequestBody Product updatedProduct) {
        String sellerId = getAuthenticatedSellerId();
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();
            if (!existingProduct.getSellerId().equals(sellerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            // Update fields
            existingProduct.setProductID(updatedProduct.getProductID());
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setProductPrice(updatedProduct.getProductPrice());
            existingProduct.setProductCount(updatedProduct.getProductCount());
            existingProduct.setImages(updatedProduct.getImages());
            existingProduct.setSellerId(sellerId); // Ensure sellerId remains unchanged
            Product savedProduct = productRepository.save(existingProduct);
            return ResponseEntity.ok(savedProduct);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        String sellerId = getAuthenticatedSellerId();
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (!product.getSellerId().equals(sellerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}