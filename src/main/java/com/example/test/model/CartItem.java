package com.example.test.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cartItems")
public class CartItem {
    @Id
    private String id;
    private String userId;
    private String productId;
    private String productName;
    private double productPrice;
    private int quantity;
    private String image;

    // Constructors
    public CartItem() {}

    public CartItem(String userId, String productId, String productName, double productPrice, int quantity, String image) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.image = image;
    }

    // Getters and Setters
}