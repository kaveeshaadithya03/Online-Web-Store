package com.example.test.controller;

import com.example.test.model.User;
import com.example.test.model.Product;
import com.example.test.repository.UserRepository;
import com.example.test.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @Valid @RequestBody User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User existingUserWithEmail = userRepository.findByEmail(updatedUser.getEmail());
        if (existingUserWithEmail != null && !existingUserWithEmail.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        updatedUser.setId(id);
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        } else {
            updatedUser.setPassword(existingUser.get().getPassword());
        }
        User savedUser = userRepository.save(updatedUser);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/products")
    @PreAuthorize("hasAuthority('ROLE_seller')")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null || !"seller".equals(user.getRole())) {
            return ResponseEntity.badRequest().body("Invalid seller email");
        }
        if (productRepository.existsByProductID(product.getProductID())) {
            return ResponseEntity.badRequest().body("Product ID already exists");
        }
        product.setSellerId(user.getId());
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasAuthority('ROLE_seller')")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @Valid @RequestBody Product updatedProduct) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null || !"seller".equals(user.getRole())) {
            return ResponseEntity.badRequest().body("Invalid seller email");
        }
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getId().equals(existingProduct.get().getSellerId())) {
            return ResponseEntity.badRequest().body("Unauthorized: Product does not belong to this seller");
        }
        updatedProduct.setId(id);
        updatedProduct.setSellerId(user.getId());
        Product savedProduct = productRepository.save(updatedProduct);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/products")
    @PreAuthorize("hasAuthority('ROLE_seller')")
    public List<Product> getSellerProducts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        return productRepository.findBySellerId(user.getId());
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAuthority('ROLE_seller')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null || !"seller".equals(user.getRole())) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getId().equals(product.get().getSellerId())) {
            return ResponseEntity.badRequest().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}