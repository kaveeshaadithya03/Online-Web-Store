package com.example.test.controller;

import com.example.test.model.CartItem;
import com.example.test.repository.CartItemRepository;
import com.example.test.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody CartItem cartItem) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Verify product exists and has sufficient stock
        Optional<com.example.test.model.Product> productOptional = productRepository.findById(cartItem.getProductId());
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        com.example.test.model.Product product = productOptional.get();
        if (product.getProductCount() < cartItem.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        cartItem.setProductName(product.getProductName());
        cartItem.setProductPrice(product.getProductPrice());
        cartItem.setImage(product.getImages());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
            cartItemRepository.save(item);
            return ResponseEntity.ok(item);
        }
        CartItem savedItem = cartItemRepository.save(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart() {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable String id, @RequestBody CartItem updatedItem) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!cartItemOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Verify product stock
        Optional<com.example.test.model.Product> productOptional = productRepository.findById(updatedItem.getProductId());
        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        com.example.test.model.Product product = productOptional.get();
        if (product.getProductCount() < updatedItem.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        CartItem cartItem = cartItemOptional.get();
        cartItem.setQuantity(updatedItem.getQuantity());
        cartItemRepository.save(cartItem);
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@PathVariable String id) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (cartItemOptional.isPresent()) {
            cartItemRepository.delete(cartItemOptional.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        cartItemRepository.deleteAll(cartItems);
        return ResponseEntity.ok().build();
    }
}